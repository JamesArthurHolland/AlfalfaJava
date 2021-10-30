# ./Dockerfile

# Extend from the official Elixir image
FROM elixir:latest

ARG DIR="."

RUN apt-get update && \
  apt-get install -y postgresql-client
#  apt-get install -y default-mysql-client

# Create app directory and copy the Elixir projects into it
RUN mkdir /app
COPY $DIR /app
WORKDIR /app

# Install hex package manager
# By using --force, we don’t need to type “Y” to confirm the installation
RUN mix local.hex --force
RUN mix local.rebar --force

# Compile the project
RUN mix do deps.get, compile

CMD ["/app/entrypoint.sh"]
