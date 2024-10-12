#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <unordered_map>
#include <cmath>
#include <ctime>
#include <regex>

using namespace std;

class User
{
private:
    string username;
    string password;

public:
    string get_username()
    {
        return username;
    }

    bool check_strong_password(const string &password)
    {
        if (password.length() < 8)
        {
            return false;
        }

        bool has_upper = false;
        bool has_lower = false;
        bool has_digit = false;
        bool has_special_ch = false;

        const string Special_character = "!@#$%^&*+=:-_<>";

        for (char ch : password)
        {
            if (isupper(ch))
            {
                has_upper = true;
            }
            else if (islower(ch))
            {
                has_lower = true;
            }
            else if (isdigit(ch))
            {
                has_digit = true;
            }
            else if (Special_character.find(ch) != string::npos)
            {
                has_special_ch = true;
            }
        }

        return has_upper && has_digit && has_lower && has_special_ch;
    }

    string hash_password(const string &password)
    {
        return to_string(hash<string>{}(password));
    }

    void register_user()
    {
        cout << "Enter Your UserName" << endl;
        cin >> username;

        while (true)
        {
            cout << "Enter Your Strong Password" << endl;
            cin >> password;

            if (!check_strong_password(password))
            {
                cout << "Password is not Strong Please provide a strong password" << endl;
                cout << "----------------------------------------" << endl;
            }
            else
            {
                string hashed_password = hash_password(password);

                ofstream file("user.txt", ios::app);
                if (file.is_open())
                {
                    file << username << " " << hashed_password << endl;
                    file.close();
                    cout << "Registered Successfully" << endl;
                    cout << "----------------------------------------" << endl;
                }

                else
                {
                    cout << "Error To Register" << endl;
                    cout << "----------------------------------------" << endl;
                }
                break;
            }
        }
    }

    bool Login_User()
    {
        cout << "Enter Your UserName" << endl;
        cin >> username;

        cout << "Enter Your Strong Password" << endl;
        cin >> password;

        ifstream file("user.txt");
        string filename, filepassword;

        if (file.is_open())
        {
            while (file >> filename >> filepassword)
            {
                if (filename == username && filepassword == hash_password(password))
                {
                    cout << "Login Successfully" << endl;
                    cout << "----------------------------------------" << endl;
                    return true;
                }
            }
            file.close();
        }
        cout << "Invalid user input" << endl;
        return false;
    }

    void user_handle_Input()
    {
        int section;

        cout << "1. Resgiter" << endl;
        cout << "2. Login" << endl;

        cout << "Enter The Section" << endl;
        cin >> section;

        if (section == 1)
        {
            register_user();
        }
        else if (section == 2)
        {
            Login_User();
        }
        else
        {
            cout << "Invalid Section" << endl;
            cout << "----------------------------------------" << endl;
        }
    }
};

// class for menuitem
class Menuitem
{
public:
    string name;
    double price;

    Menuitem(string n, double p) : name(n), price(p) {}
};

// class to manage to  a user cart

class Cart
{
    vector<Menuitem> items;
    string username;

    void save_cart_state()
    {
        ofstream file(username + "cart.txt");
        if (file.is_open())
        {
            for (const auto &item : items)
            {
                file << item.name << " " << item.price << endl;
            }
        }
        file.close();
    }

    void load_cart_state()
    {
        ifstream file(username + "cart.txt");
        if (file.is_open())
        {
            string itemname;
            double price;

            while (file >> itemname >> price)
            {
                items.push_back(Menuitem(itemname, price));
            }
        }
        file.close();
    }

public:
    Cart(const string &user) : username(user)
    {
        load_cart_state();
    }

    void additem(const Menuitem &item)
    {
        items.push_back(item);
        cout << item.name << " Added to cart " << endl;
        save_cart_state();
    }

    void removeitem(string itemname)
    {
        for (auto it = items.begin(); it != items.end(); it++)
        {
            if (it->name == itemname)
            {
                cout << it->name << "Item removed from cart" << endl;
                items.erase(it);
                save_cart_state();
                break;
            }
        }
    }

    void viewcart()
    {
        cout << "Items In the cart: " << endl;
        for (const auto &item : items)
        {
            cout << item.name << "-₹ " << item.price << endl;
        }
    }

    double calculation()
    {
        double total = 0;

        for (const auto &item : items)
        {
            total += item.price;
        }
        return total;
    }

    vector<Menuitem> get_item()
    {
        return items;
    }

    void clear_cart()
    {
        ofstream file(username + "cart.txt", ios::trunc);
        file.close();
    }
};

// Managing the entire food ordring system

class online_food_ordering_system
{
    vector<Menuitem> menu;
    Cart cart;
    User current_user;

public:
    online_food_ordering_system(User user) : current_user(user), cart(user.get_username())
    {
        menu.push_back(Menuitem("Masala Dosai", 35));
        menu.push_back(Menuitem("Parota", 40));
        menu.push_back(Menuitem("Mini Tiffen", 80));
        menu.push_back(Menuitem("Special Pongal", 42));
    }

    void displaymenu()
    {
        cout << "Delicious Menu" << endl;
        for (int i = 0; i < menu.size(); i++)
        {
            cout << i + 1 << " " << menu[i].name << "-₹ " << menu[i].price << endl;
        }
    }

    void orderfood()
    {
        int choice;
        while (true)
        {
            displaymenu();
            cout << "Enter the number of the item shown in the Menu you want to add in the cart (0 to Checkout)" << endl;
            cin >> choice;

            if (choice == 0)
            {
                break;
            }

            if (choice > 0 && choice <= menu.size())
            {
                cart.additem(menu[choice - 1]);
                cout << "----------------------------------------" << endl;
            }
            else if (choice < 0 && abs(choice) <= menu.size())
            {
                cart.removeitem(menu[abs(choice) - 1].name);
                cout << "----------------------------------------" << endl;
            }
            else
            {
                cout << "Invalid input please provide a valid input" << endl;
                cout << "----------------------------------------" << endl;
            }
        }
        checkout();
    }

    void checkout()
    {
        cart.viewcart();
        cout << "----------------------------------------" << endl;
        double total_amount = cart.calculation();
        cout << "Total amount: ₹ " << cart.calculation() << endl;
        cout << "Thank you for your order" << endl;

        save_cart_history(total_amount);
    }

    void save_cart_history(double total_amount)
    {
        ofstream file(current_user.get_username() + "History.txt");
        if (file.is_open())
        {
            time_t now = time(0);
            string dt = ctime(&now);

            file << "Order date: " << dt << endl;
            file << "Items Ordered" << endl;

            for (const auto &item : cart.get_item())
            {
                file << item.name << "- ₹ " << item.price << endl;
                cout << "----------------------------------------" << endl;
            }

            file << " Total: ₹ " << total_amount << endl;
            file.close();
            cout << "order History Successfully saved" << endl;
            cout << "----------------------------------------" << endl;
        }
        else
        {
            cout << "Error In saving Order History" << endl;
        }
    }
};

int main()
{
    User user;
    user.user_handle_Input();
    online_food_ordering_system system(user);
    system.orderfood();
    return 0;
}