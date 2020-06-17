defmodule ElixirRestWeb.Schema.Types.UserType do
  use Absinthe.Schema.Notation

  object :user_type do
    field(:id, :id)
    field(:business_name, :string)
    field(:confirmation_code, :string)
    field(:email, :string)
    field(:first_name, :string)
    field(:industry, :string)
    field(:last_name, :string)
    field(:phone_number, :string)
    field(:use_case_description, :string)
  end

  # TODO validation of nullables
  input_object :user_input_type do
    field(:business_name, :string)
    field(:confirmation_code, :string)
    field(:email, :string)
    field(:first_name, :string)
    field(:industry, :string)
    field(:last_name, :string)
    field(:phone_number, :string)
    field(:use_case_description, :string)
  end
end