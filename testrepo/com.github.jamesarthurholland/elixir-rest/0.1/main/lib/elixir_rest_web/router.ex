defmodule ElixirRestWeb.Router do
  use ElixirRestWeb, :router

  pipeline :api do
    plug(:accepts, ["json"])
  end

  scope "/api" do
    pipe_through(:api)

    forward("/graphql", Absinthe.Plug, schema: ElixirRestWeb.Schema, json_codec: Poison)

    if Mix.env() == :dev do
      forward("/graphiql", Absinthe.Plug.GraphiQL, schema: ElixirRestWeb.Schema, json_codec: Poison)
    end
  end
end
