defmodule {{Project}}.Repo do
  use Ecto.Repo, otp_app: :{{pro_ject}}

  @doc """
  Dynamically loads the repository url from the
  DATABASE_URL environment variable.
  """
  def init(_, opts) do

    opts = Confex.Resolver.resolve!(opts)

    {:ok, opts}
  end
end