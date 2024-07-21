#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "app" <<-EOSQL
	    DO \$\$
	    BEGIN
	        IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'app') THEN
	            PERFORM dblink_exec('dbname=' || current_database(), 'CREATE DATABASE app');
	        END IF;
	    END
	    \$\$;
EOSQL
