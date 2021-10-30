defmodule {{Project}}.UsersTest do
  use {{Project}}.DataCase

  alias {{Project}}.Users

  describe "users" do
    alias {{Project}}.Users.User

    @valid_attrs %{
      "business_name,": "some business_name,",
      "confirmation_code,": "some confirmation_code,",
      "email,": "some email,",
      "first_name,": "some first_name,",
      "industry,": "some industry,",
      "last_name,": "some last_name,",
      "phone_number,": "some phone_number,",
      use_case_description: "some use_case_description"
    }
    @update_attrs %{
      "business_name,": "some updated business_name,",
      "confirmation_code,": "some updated confirmation_code,",
      "email,": "some updated email,",
      "first_name,": "some updated first_name,",
      "industry,": "some updated industry,",
      "last_name,": "some updated last_name,",
      "phone_number,": "some updated phone_number,",
      use_case_description: "some updated use_case_description"
    }
    @invalid_attrs %{
      "business_name,": nil,
      "confirmation_code,": nil,
      "email,": nil,
      "first_name,": nil,
      "industry,": nil,
      "last_name,": nil,
      "phone_number,": nil,
      use_case_description: nil
    }

    def user_fixture(attrs \\ %{}) do
      {:ok, user} =
        attrs
        |> Enum.into(@valid_attrs)
        |> Users.create_user()

      user
    end

    test "list_users/0 returns all users" do
      user = user_fixture()
      assert Users.list_users() == [user]
    end

    test "get_user!/1 returns the user with given id" do
      user = user_fixture()
      assert Users.get_user!(user.id) == user
    end

    test "create_user/1 with valid data creates a user" do
      assert {:ok, %User{} = user} = Users.create_user(@valid_attrs)
      assert user.business_name == "some business_name,"
      assert user.confirmation_code == "some confirmation_code,"
      assert user.email == "some email,"
      assert user.first_name == "some first_name,"
      assert user.industry == "some industry,"
      assert user.last_name == "some last_name,"
      assert user.phone_number == "some phone_number,"
      assert user.use_case_description == "some use_case_description"
    end

    test "create_user/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Users.create_user(@invalid_attrs)
    end

    test "update_user/2 with valid data updates the user" do
      user = user_fixture()
      assert {:ok, user} = Users.update_user(user, @update_attrs)
      assert %User{} = user
      assert user.business_name == "some updated business_name"
      assert user.confirmation_code == "some updated confirmation_code"
      assert user.email == "some updated email"
      assert user.first_name == "some updated first_name"
      assert user.industry == "some updated industry"
      assert user.last_name == "some updated last_name"
      assert user.phone_number == "some updated phone_number"
      assert user.use_case_description == "some updated use_case_description"
    end

    test "update_user/2 with invalid data returns error changeset" do
      user = user_fixture()
      assert {:error, %Ecto.Changeset{}} = Users.update_user(user, @invalid_attrs)
      assert user == Users.get_user!(user.id)
    end

    test "delete_user/1 deletes the user" do
      user = user_fixture()
      assert {:ok, %User{}} = Users.delete_user(user)
      assert_raise Ecto.NoResultsError, fn -> Users.get_user!(user.id) end
    end

    test "change_user/1 returns a user changeset" do
      user = user_fixture()
      assert %Ecto.Changeset{} = Users.change_user(user)
    end
  end

  describe "users" do
    alias {{Project}}.Users.User

    @valid_attrs %{
      business_name: "some business_name",
      confirmation_code: "some confirmation_code",
      email: "some email",
      first_name: "some first_name",
      industry: "some industry",
      last_name: "some last_name",
      phone_number: "some phone_number",
      use_case_description: "some use_case_description"
    }
    @update_attrs %{
      business_name: "some updated business_name",
      confirmation_code: "some updated confirmation_code",
      email: "some updated email",
      first_name: "some updated first_name",
      industry: "some updated industry",
      last_name: "some updated last_name",
      phone_number: "some updated phone_number",
      use_case_description: "some updated use_case_description"
    }
    @invalid_attrs %{
      business_name: nil,
      confirmation_code: nil,
      email: nil,
      first_name: nil,
      industry: nil,
      last_name: nil,
      phone_number: nil,
      use_case_description: nil
    }

    def user_fixture(attrs \\ %{}) do
      {:ok, user} =
        attrs
        |> Enum.into(@valid_attrs)
        |> Users.create_user()

      user
    end

    test "list_users/0 returns all users" do
      user = user_fixture()
      assert Users.list_users() == [user]
    end

    test "get_user!/1 returns the user with given id" do
      user = user_fixture()
      assert Users.get_user!(user.id) == user
    end

    test "create_user/1 with valid data creates a user" do
      assert {:ok, %User{} = user} = Users.create_user(@valid_attrs)
      assert user.business_name == "some business_name"
      assert user.confirmation_code == "some confirmation_code"
      assert user.email == "some email"
      assert user.first_name == "some first_name"
      assert user.industry == "some industry"
      assert user.last_name == "some last_name"
      assert user.phone_number == "some phone_number"
      assert user.use_case_description == "some use_case_description"
    end

    test "create_user/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Users.create_user(@invalid_attrs)
    end

    test "update_user/2 with valid data updates the user" do
      user = user_fixture()
      assert {:ok, user} = Users.update_user(user, @update_attrs)
      assert %User{} = user
      assert user.business_name == "some updated business_name"
      assert user.confirmation_code == "some updated confirmation_code"
      assert user.email == "some updated email"
      assert user.first_name == "some updated first_name"
      assert user.industry == "some updated industry"
      assert user.last_name == "some updated last_name"
      assert user.phone_number == "some updated phone_number"
      assert user.use_case_description == "some updated use_case_description"
    end

    test "update_user/2 with invalid data returns error changeset" do
      user = user_fixture()
      assert {:error, %Ecto.Changeset{}} = Users.update_user(user, @invalid_attrs)
      assert user == Users.get_user!(user.id)
    end

    test "delete_user/1 deletes the user" do
      user = user_fixture()
      assert {:ok, %User{}} = Users.delete_user(user)
      assert_raise Ecto.NoResultsError, fn -> Users.get_user!(user.id) end
    end

    test "change_user/1 returns a user changeset" do
      user = user_fixture()
      assert %Ecto.Changeset{} = Users.change_user(user)
    end
  end
end
