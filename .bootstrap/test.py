#!/usr/bin/python3

import argparse
import os
import socket
import subprocess
import sys


dbConn = {}


def executeCommand(cmd, input=None, cwd=None, env=None, rstrp=True, timeout=None):
    print("Cmd: " + cmd)

    pipe = subprocess.Popen(
        cmd,
        shell=True,
        cwd=cwd,
        env=env,
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
        stdin=subprocess.PIPE if input is not None else None
    )

    try:
        (output, errout) = pipe.communicate(input=input, timeout=timeout)
    except subprocess.TimeoutExpired:
        pipe.kill()
        (output, errout) = pipe.communicate()
        result = output.decode("utf8", errors="replace")
        print("ERROR: Command timed out: " + cmd)

        if result.strip() != "":
            print(result)

        sys.exit(1)

    status = pipe.returncode

    result = output.decode("utf8", errors="replace")
    if rstrp:
        result = result.rstrip()

    if status != 0:
        print("ERROR: Failed to execute the command: " + cmd)

        if result.strip() != "":
            print(result)

        sys.exit(1)

    return (status, result)


def deriveUsername(apiUserid):
    apiUserid = str(apiUserid).strip()

    if apiUserid == "":
        return ""

    if "_" in apiUserid:
        prefix = apiUserid.split("_")[0]
    else:
        prefix = apiUserid

    return prefix.upper() + "API"


def sqlEscape(value):
    if value is None:
        return ""

    return str(value).replace("\\", "\\\\").replace("'", "''")


def executeMysqlStep(stepName, sql, mysqlBaseCmd, mysqlEnv, targetDb, timeout=60):
    print("")
    print("========================================")
    print("RUNNING SQL STEP: " + stepName)
    print("========================================")

    cmd = mysqlBaseCmd + "--database=" + targetDb

    (status, result) = executeCommand(
        cmd,
        input=(sql.strip() + "\n").encode("utf8"),
        env=mysqlEnv,
        rstrp=False,
        timeout=timeout
    )

    if result.strip() != "":
        print(result)

    print("COMPLETED SQL STEP: " + stepName)
    return (status, result)


def executeMysqlSteps(stepList, mysqlBaseCmd, mysqlEnv, targetDb):
    for step in stepList:
        stepName = step[0]
        stepSql = step[1]
        executeMysqlStep(stepName, stepSql, mysqlBaseCmd, mysqlEnv, targetDb)


def buildEnvSqlSteps():
    steps = []

    steps.append((
        "env_01_insert_urimaster",
        """
SELECT 'STARTING ENV STEP 1: URIMASTER' AS status;

SELECT @uri_id := COALESCE(MAX(uri_id), 0) + 1
FROM urimaster;

INSERT INTO urimaster
(
    uri_id,
    uri_pattern,
    uri_desc,
    uri_auth_type,
    active,
    created_by,
    created_date,
    modified_by,
    modified_date,
    http_method
)
VALUES
(
    @uri_id,
    '/ws/trade-input/*',
    'Trade input API',
    'group',
    1,
    NULL,
    NOW(),
    NULL,
    NOW(),
    'ALL'
);

SELECT 'VERIFY URIMASTER' AS status;
SELECT * FROM urimaster WHERE uri_id = @uri_id;
"""
    ))

    steps.append((
        "env_02_insert_groupmaster",
        """
SELECT 'STARTING ENV STEP 2: GROUPMASTER' AS status;

SELECT @group_id := COALESCE(MAX(group_id), 0) + 1
FROM groupmaster;

INSERT INTO groupmaster
(
    group_id,
    app_id,
    group_name,
    group_code,
    group_desc,
    group_type,
    ui_display,
    active,
    created_by,
    created_date,
    modified_by,
    modified_date,
    mfa_level
)
VALUES
(
    @group_id,
    10,
    'cls_trade_api',
    'cls_trade_api',
    'cls_trade_api',
    NULL,
    1,
    1,
    NULL,
    NOW(),
    NULL,
    NOW(),
    0
);

SELECT 'VERIFY GROUPMASTER' AS status;
SELECT * FROM groupmaster WHERE group_id = @group_id;
"""
    ))

    steps.append((
        "env_03_insert_group_uri_xref",
        """
SELECT 'STARTING ENV STEP 3: GROUP_URI_XREF' AS status;

SELECT @uri_id := uri_id
FROM urimaster
WHERE uri_pattern = '/ws/trade-input/*'
ORDER BY uri_id DESC
LIMIT 1;

SELECT @group_id := group_id
FROM groupmaster
WHERE group_name = 'cls_trade_api'
ORDER BY group_id DESC
LIMIT 1;

SELECT @group_uri_id := COALESCE(MAX(group_uri_id), 0) + 1
FROM group_uri_xref;

INSERT INTO group_uri_xref
(
    group_uri_id,
    group_id,
    uri_id,
    created_by,
    created_date,
    modified_by,
    modified_date
)
VALUES
(
    @group_uri_id,
    @group_id,
    @uri_id,
    NULL,
    NOW(),
    NULL,
    NULL
);

SELECT 'VERIFY GROUP_URI_XREF' AS status;
SELECT * FROM group_uri_xref WHERE group_uri_id = @group_uri_id;
"""
    ))

    return steps


def buildUserSqlSteps(participantId, apiUserid, username):
    participantId = sqlEscape(participantId)
    apiUserid = sqlEscape(apiUserid)
    username = sqlEscape(username)

    steps = []

    steps.append((
        "user_01_lookup_organization",
        """
SELECT 'STARTING USER STEP 1: ORG LOOKUP' AS status;

SELECT organization_id
FROM organizationmaster
WHERE participant_id = '%s'
LIMIT 1;
""" % participantId
    ))

    steps.append((
        "user_02_insert_usermaster",
        """
SELECT 'STARTING USER STEP 2: USERMASTER' AS status;

SELECT @organization_id := organization_id
FROM organizationmaster
WHERE participant_id = '%s'
LIMIT 1;

SELECT 'ORG LOOKUP' AS status, @organization_id AS organization_id;

SELECT @random_uuid := UUID();

INSERT INTO usermaster
(
    id,
    login,
    display_name,
    first_name,
    last_name,
    middle_name,
    honorific_prefix,
    honorific_suffix,
    email,
    mobile_phone,
    user_type,
    department,
    role,
    status,
    modified_by,
    allow_multi_session,
    organization_id,
    enforce_internal_network
)
VALUES
(
    @random_uuid,
    '%s@cl3-services.com',
    '%s',
    '%s',
    '%s',
    NULL,
    NULL,
    NULL,
    'noreply@cl3-bank.com',
    NULL,
    'API',
    'Operations',
    NULL,
    1,
    NULL,
    0,
    @organization_id,
    0
);

SELECT 'VERIFY USERMASTER' AS status;
SELECT * FROM usermaster WHERE id = @random_uuid;
""" % (
            participantId,
            apiUserid,
            username,
            username,
            username
        )
    ))

    steps.append((
        "user_03_insert_group_user_xref",
        """
SELECT 'STARTING USER STEP 3: GROUP_USER_XREF' AS status;

SELECT @user_id := id
FROM usermaster
WHERE login = '%s@cl3-services.com'
ORDER BY id DESC
LIMIT 1;

SELECT @existing_group_id := group_id
FROM groupmaster
WHERE group_name = 'cls_trade_api'
ORDER BY group_id DESC
LIMIT 1;

SELECT @group_user_id := COALESCE(MAX(group_user_id), 0) + 1
FROM group_user_xref;

INSERT INTO group_user_xref
(
    group_user_id,
    group_id,
    user_id,
    created_by,
    created_date,
    modified_by,
    modified_date
)
VALUES
(
    @group_user_id,
    @existing_group_id,
    @user_id,
    NULL,
    NOW(),
    NULL,
    NULL
);

SELECT 'VERIFY GROUP_USER_XREF' AS status;
SELECT * FROM group_user_xref WHERE group_user_id = @group_user_id;
""" % apiUserid
    ))

    return steps


def setupDBConnection(runEnv, runUser, participantId, apiUserid):
    global dbConn

    try:
        fqdn = socket.gethostname().strip().lower()
        print("hostname: " + fqdn)

        if "cit" in fqdn:
            env_root = "cit"
        elif "dev" in fqdn:
            env_root = "dev"
        else:
            print("ERROR: Unable to determine env_root from hostname")
            sys.exit(1)

        print("env_root: " + env_root)

        nslookupHost = "rds-aurora-mysql." + env_root + ".clsnet"
        nslookupCmd = (
            "nslookup " + nslookupHost +
            " | grep canonical | head -1 | sed 's/.$//' | awk '{print $5}'"
        )
        (status, rdsHost) = executeCommand(nslookupCmd, timeout=30)

        rdsHost = rdsHost.strip()
        if rdsHost == "":
            print("ERROR: Failed to resolve RDS host from: " + nslookupHost)
            sys.exit(1)

        print("RDSHOST: " + rdsHost)

        dbUser = "aducpnet"
        dbPort = "3306"
        sslCa = "/cls/app1/env/truststore/eu-west-2-bundle_RSA2048.pem"

        tokenCmd = (
            "aws rds generate-db-auth-token "
            "--hostname " + rdsHost + " "
            "--port " + dbPort + " "
            "--username " + dbUser
        )
        (status, token) = executeCommand(tokenCmd, timeout=30)

        token = token.strip()
        if token == "":
            print("ERROR: Failed to generate DB auth token")
            sys.exit(1)

        mysqlEnv = os.environ.copy()
        mysqlEnv["MYSQL_PWD"] = token

        mysqlBaseCmd = (
            "mysql "
            "--host=" + rdsHost + " "
            "--port=" + dbPort + " "
            "--ssl-ca=" + sslCa + " "
            "--user=" + dbUser + " "
            "--enable-cleartext-plugin "
            "--batch "
            "--raw "
        )

        dbConn = {
            "hostname": fqdn,
            "env_root": env_root,
            "rds_host": rdsHost,
            "db_user": dbUser,
            "db_port": dbPort,
            "ssl_ca": sslCa,
            "mysql_env": mysqlEnv,
            "mysql_base_cmd": mysqlBaseCmd
        }

        findDbCmd = mysqlBaseCmd + '-N -B -e "show databases;"'
        (status, result) = executeCommand(findDbCmd, env=mysqlEnv, timeout=30)

        dbList = [line.strip() for line in result.splitlines() if line.strip()]
        ucpDbs = [db for db in dbList if "ucp" in db.lower()]

        if len(ucpDbs) == 0:
            print("ERROR: No DB found with 'ucp' in it")
            sys.exit(1)

        exactMatches = [db for db in ucpDbs if db.lower() == "ucp"]
        if len(exactMatches) > 0:
            targetDb = exactMatches[0]
        else:
            targetDb = ucpDbs[0]

        print("Selected DB: " + targetDb)

        if not runEnv and not runUser:
            print("ERROR: Nothing to run. Use --env and/or --user")
            sys.exit(1)

        if runEnv:
            envSteps = buildEnvSqlSteps()
            executeMysqlSteps(envSteps, mysqlBaseCmd, mysqlEnv, targetDb)

        if runUser:
            if participantId is None or str(participantId).strip() == "":
                print("ERROR: participant_id is required when --user is set")
                sys.exit(1)

            if apiUserid is None or str(apiUserid).strip() == "":
                print("ERROR: api_userid is required when --user is set")
                sys.exit(1)

            username = deriveUsername(apiUserid)
            print("USERNAME: " + username)

            userSteps = buildUserSqlSteps(participantId, apiUserid, username)
            executeMysqlSteps(userSteps, mysqlBaseCmd, mysqlEnv, targetDb)

    except Exception as e:
        print("ERROR: setupDBConnection failed: " + str(e))
        sys.exit(1)


def main():
    parser = argparse.ArgumentParser(description="Trade API onboarding")
    parser.add_argument("--env", action="store_true", help="run environment queries")
    parser.add_argument("--user", action="store_true", help="run user queries")
    parser.add_argument("participant_id", nargs="?", help="participant id")
    parser.add_argument("api_userid", nargs="?", help="api userid")

    pa = parser.parse_args()

    if not pa.env and not pa.user:
        print("ERROR: Use --env and/or --user")
        sys.exit(1)

    setupDBConnection(pa.env, pa.user, pa.participant_id, pa.api_userid)


if __name__ == "__main__":
    main()