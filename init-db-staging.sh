#!/bin/bash

set -e

# Create the admindb database
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE $POSTGRES_STAGING_DB;
EOSQL
