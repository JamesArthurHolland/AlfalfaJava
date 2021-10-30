defmodule {{Project}}.Repo.Migrations.CreateUsers do
  use Ecto.Migration

  def change do
    create table(:users) do
      add :first_name, :string
      add :last_name, :string
      add :business_name, :string
      add :email, :string
      add :confirmation_code, :string
      add :phone_number, :string
      add :industry, :string
      add :use_case_description, :string

      timestamps()
    end

  end
end
