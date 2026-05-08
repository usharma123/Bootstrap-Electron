def getDatabaseInfoFromEnvInfo():
    envInfoPatterns = [
        "/cls/appl/env/*envInfo.json",
        "/cls/appl/env/*.envInfo.json"
    ]

    envInfoFiles = []

    for pattern in envInfoPatterns:
        envInfoFiles.extend(glob.glob(pattern))

    envInfoFiles = sorted(set(envInfoFiles))

    if len(envInfoFiles) == 0:
        print("ERROR: No envInfo json file found under /cls/appl/env/")
        sys.exit(1)

    candidates = []

    for envInfoFile in envInfoFiles:
        try:
            with open(envInfoFile, "r") as f:
                data = json.load(f)

            if "databaseName" in data and str(data["databaseName"]).strip() != "":
                databaseName = str(data["databaseName"]).strip().lower()
                envRoot = getEnvRootFromDatabaseName(databaseName)

                candidates.append({
                    "file": envInfoFile,
                    "env_root": envRoot,
                    "database_name": databaseName
                })

        except Exception as e:
            print("WARNING: Unable to read envInfo file: " + envInfoFile + " error: " + str(e))

    if len(candidates) == 0:
        print("ERROR: No envInfo file contains databaseName")
        sys.exit(1)

    uniqueDatabaseNames = sorted(set([candidate["database_name"] for candidate in candidates]))

    if len(uniqueDatabaseNames) > 1:
        print("ERROR: Multiple databaseName values found. Cannot choose safely.")
        for candidate in candidates:
            print("  " + candidate["file"] + " -> " + candidate["database_name"])
        sys.exit(1)

    selectedDatabaseName = uniqueDatabaseNames[0]
    selectedEnvRoot = getEnvRootFromDatabaseName(selectedDatabaseName)

    print("Env info files checked:")
    for candidate in candidates:
        print("  " + candidate["file"] + " -> " + candidate["database_name"])

    print("Database name from envInfo: " + selectedDatabaseName)
    print("Env root derived from databaseName: " + selectedEnvRoot)

    return {
        "env_root": selectedEnvRoot,
        "database_name": selectedDatabaseName
    }


def checkDatabaseExists(mysqlBaseCmd, mysqlEnv, databaseName):
    sql = """
SELECT SCHEMA_NAME
FROM INFORMATION_SCHEMA.SCHEMATA
WHERE SCHEMA_NAME = '%s';
""" % sqlEscape(databaseName)

    cmd = mysqlBaseCmd + "-N -B "

    (status, result) = executeCommand(
        cmd,
        input=(sql.strip() + "\n").encode("utf8"),
        env=mysqlEnv,
        timeout=30
    )

    return result.strip() == databaseName