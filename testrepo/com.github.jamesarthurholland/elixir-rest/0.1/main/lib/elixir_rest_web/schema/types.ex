defmodule ElixirRestWeb.Schema.Types do
  use Absinthe.Schema.Notation

  alias ElixirRestWeb.Schema.Types

  import_types(Types.UserType)
end