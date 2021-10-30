#!/bin/bash
# Docker entrypoint script.
# Wait until Postgres is ready
while ! pg_isready -q -h ${{PROJECT}}_DB_HOST -p ${{PROJECT}}_DB_PORT -U ${{PROJECT}}_DB_USERNAME
do
  echo "$(date) - waiting for database to start ${{PROJECT}}_DB_HOST ${{PROJECT}}_DB_PORT. User: ${{PROJECT}}_DB_USERNAME"
  sleep 2
done

# Create, migrate, and seed database if it doesn't exist.
#if [[ -z `psql -Atqc "\\list $RDS_DATABASE"` ]]; then
#  echo "Database $RDS_DATABASE does not exist. Creating..."
#  createdb -E UTF8 $RDS_DATABASE -l en_US.UTF-8 -T template0
#  mix ecto.migrate
#  mix run priv/repo/seeds.exs
#  echo "Database $RDS_DATABASE created."
#fi

exec mix phx.server
