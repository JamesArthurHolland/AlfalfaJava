use Mix.Config

# In this file, we keep production configuration that
# you'll likely want to automate and keep away from
# your version control system.
#
# You should document the content of this
# file or create a script for recreating it, since it's
# kept out of version control and might be hard to recover
# or recreate for your teammates (or yourself later on).
config :{{pro_ject}}, {{Project}}Web.Endpoint,
  secret_key_base: "L1ksRmnhL9SMCQzjDP4WX9AUzmuBJtmQVFHp/aV5G+WyXLzGHmBA/kLR7+CcKX4O"

# Configure your database
config :{{pro_ject}}, {{Project}}.Repo,
  adapter: Ecto.Adapters.Postgres,
  username: "postgres",
  password: "postgres",
  database: "{{pro_ject}}_prod",
  pool_size: 15
