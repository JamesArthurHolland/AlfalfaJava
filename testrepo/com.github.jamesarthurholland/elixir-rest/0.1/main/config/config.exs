# This file is responsible for configuring your application
# and its dependencies with the aid of the Mix.Config module.
#
# This configuration file is loaded before any dependency and
# is restricted to this project.
use Mix.Config

# General application configuration
config :elixir_rest,
  ecto_repos: [ElixirRest.Repo]

# Configures the endpoint
config :elixir_rest, ElixirRestWeb.Endpoint,
  url: [host: "localhost"],
  secret_key_base: "D6jNSw4e7We5y7AAg+MqLpVZ1ureygTMQ+deek1y58N+2HJWzo/HDqby6lvBL6Ah",
  render_errors: [view: ElixirRestWeb.ErrorView, accepts: ~w(json)],
  pubsub: [name: ElixirRest.PubSub, adapter: Phoenix.PubSub.PG2]

# Configures Elixir's Logger
config :logger, :console,
  format: "$time $metadata[$level] $message\n",
  metadata: [:user_id]

# Import environment specific config. This must remain at the bottom
# of this file so it overrides the configuration defined above.
import_config "#{Mix.env()}.exs"
