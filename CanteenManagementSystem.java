import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
// The Abstract Window Toolkit (AWT) is Java's original platform-dependent windowing, graphics,
// and user-interface widget toolkit, preceding Swing briefly it is standard api provide gui

class MenuItem {
    private String name;
    private double price;

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

class CartItem {
    private MenuItem item;
    public int quantity;

    public CartItem(MenuItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public MenuItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }
}

class MenuItemButton extends JButton {
    private MenuItem menuItem;

    public MenuItemButton(MenuItem menuItem) {
        super(menuItem.getName());
        this.menuItem = menuItem;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }
}

public class CanteenManagementSystem extends JFrame {
    private ArrayList<MenuItem> menu = new ArrayList<>();
    private ArrayList<CartItem> cart = new ArrayList<>();

    private JTextArea cartTextArea;

    public CanteenManagementSystem() {
        Scanner sc = new Scanner(System.in);
        setTitle("Canteen Management System");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeMenu();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));

        JPanel menuPanel = new JPanel(new GridLayout(menu.size(), 1));
        for (MenuItem menuItem : menu) {
            MenuItemButton button = new MenuItemButton(menuItem);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addToCart(button.getMenuItem());
                }
            });
            menuPanel.add(button);
        }

        JScrollPane menuScrollPane = new JScrollPane(menuPanel);

        cartTextArea = new JTextArea(20, 30);
        cartTextArea.setEditable(false);
        JScrollPane cartScrollPane = new JScrollPane(cartTextArea);

        JPanel buttonPanel = new JPanel(new FlowLayout()); // Make buttons appear side by side
        JButton removeItemButton = new JButton("Remove Item");
        JButton replaceItemButton = new JButton("Replace Item");
        JButton editItemButton = new JButton("Edit Item");
        JButton showTotalButton = new JButton("Show Total");

        buttonPanel.add(removeItemButton);
        buttonPanel.add(replaceItemButton);
        buttonPanel.add(editItemButton);
        buttonPanel.add(showTotalButton);

        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);
        cartPanel.add(buttonPanel, BorderLayout.NORTH);

        panel.add(menuScrollPane);
        panel.add(cartPanel);
        add(panel); // Add the main panel to the JFrame

        removeItemButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeItem();
                updateCartTextArea();
            }
        });

        replaceItemButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                replaceItem();
                updateCartTextArea();
            }
        });

        editItemButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                editItem();
                updateCartTextArea();
            }
        });

        showTotalButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showTotalBill();
            }
        });

        setVisible(true);
    }

    private void initializeMenu() {
        // Initialize menu items
        menu.add(new MenuItem("Sandwich 🌮 - ₹350", 350));
        menu.add(new MenuItem("Burger 🍔 - ₹250", 250));
        menu.add(new MenuItem("Pizza 🍕 - ₹550", 550));
        menu.add(new MenuItem("Noodles 🍜 - ₹300", 300));
        menu.add(new MenuItem("French Fries 🍟 - ₹200", 200));
        menu.add(new MenuItem("Soft Drink 🍹 - ₹150", 150));
        menu.add(new MenuItem("Ice Cream 🍨 - ₹100", 100));
        menu.add(new MenuItem("Coffee ☕ - ₹50", 50));
        menu.add(new MenuItem("Tea ☕︎ - ₹30", 30));
        menu.add(new MenuItem("Milkshake 🍦 - ₹150", 150));
        menu.add(new MenuItem("Salad 🥗 - ₹200", 200));
        menu.add(new MenuItem("Soup 🍲 - ₹100", 100));
        menu.add(new MenuItem("Cake 🍰 - ₹250", 250));
        menu.add(new MenuItem("Cookies 🍪 - ₹500", 50));
        menu.add(new MenuItem("Donut 🍩 - ₹500", 50));
        menu.add(new MenuItem("Chocolate 🍫 - ₹50", 50));
        menu.add(new MenuItem("Muffin 🥞 - ₹50", 50));

        // Add more menu items here
    }

    private void addToCart(MenuItem menuItem) {
        String input = JOptionPane.showInputDialog(this, "Enter quantity for " + menuItem.getName() + ":");
        if (input != null) {
            try {
                int quantity = Integer.parseInt(input);
                if (quantity > 0) {
                    // Check if the item is already in the cart
                    for (CartItem cartItem : cart) {
                        if (cartItem.getItem().getName().equals(menuItem.getName())) {
                            // Update the quantity of the existing item
                            cartItem.quantity += quantity;
                            updateCartTextArea();
                            return;
                        }
                    }
                    // If the item is not in the cart, add it
                    cart.add(new CartItem(menuItem, quantity));
                    updateCartTextArea();
                } else {
                    JOptionPane.showMessageDialog(this, "Quantity must be a positive number.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removeItem() {
        JDialog dialog = new JDialog(this, "Remove Item", true);
        dialog.setLayout(new FlowLayout());

        for (CartItem cartItem : cart) {
            JButton button = new JButton(cartItem.getItem().getName());
            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    cart.remove(cartItem);
                    updateCartTextArea();
                    JOptionPane.showMessageDialog(dialog,
                            "Item: " + cartItem.getItem().getName() + " removed from cart.", "Item Removed",
                            JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                }
            });
            dialog.add(button);
        }

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void replaceItem() {
        JDialog dialog = new JDialog(this, "Replace Item", true);
        dialog.setLayout(new FlowLayout());

        for (CartItem cartItem : cart) {
            JButton button = new JButton(cartItem.getItem().getName());
            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    cart.remove(cartItem); // Remove the original item from the cart
                    dialog.dispose();
                }
            });
            dialog.add(button);
        }

        JDialog dialog2 = new JDialog(this, "Replace Item", true);
        dialog2.setLayout(new GridLayout(menu.size(), 1));

        for (MenuItem menuItem : menu) {
            JButton button = new JButton(menuItem.getName());
            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String input = JOptionPane.showInputDialog(dialog2,
                            "Enter quantity for " + menuItem.getName() + ":");
                    if (input != null) {
                        try {
                            int quantity = Integer.parseInt(input);
                            if (quantity > 0) {
                                cart.add(new CartItem(menuItem, quantity));
                                updateCartTextArea();
                                JOptionPane.showMessageDialog(dialog2,
                                        "Item Replaced : " + menuItem.getName() + " added to cart.", "Item Added",
                                        JOptionPane.INFORMATION_MESSAGE);
                                dialog2.dispose();
                            } else {
                                JOptionPane.showMessageDialog(dialog2, "Quantity must be a positive number.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(dialog2, "Invalid input.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            dialog2.add(button);
        }

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        dialog2.pack();
        dialog2.setLocationRelativeTo(this);
        dialog2.setVisible(true);
    }

    private void editItem() {
        JDialog dialog = new JDialog(this, "Edit Item", true);
        dialog.setLayout(new FlowLayout());

        for (CartItem cartItem : cart) {
            JButton button = new JButton(cartItem.getItem().getName());
            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String input = JOptionPane.showInputDialog(dialog,
                            "Enter new quantity for " + cartItem.getItem().getName() + ":");
                    if (input != null) {
                        try {
                            int quantity = Integer.parseInt(input);
                            if (quantity > 0) {
                                cartItem.quantity = quantity;
                                updateCartTextArea();
                                JOptionPane.showMessageDialog(dialog,
                                        "Quantity for " + cartItem.getItem().getName() + " updated.",
                                        "Quantity Updated",
                                        JOptionPane.INFORMATION_MESSAGE);
                                dialog.dispose();
                            } else {
                                JOptionPane.showMessageDialog(dialog, "Quantity must be a positive number.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(dialog, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            dialog.add(button);
        }

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showTotalBill() {
        double total = 0.0;
        for (CartItem item : cart) {
            total += item.getItem().getPrice() * item.getQuantity();
        }
        JOptionPane.showMessageDialog(this, "Total Bill: ₹" + total, "Total Bill", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(this, "Thanks For Shopping, Have a Great Day 😊", "Thank You",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateCartTextArea() {
        StringBuilder sb = new StringBuilder();
        double total = 0.0;
        sb.append("Cart:\n\n");
        for (CartItem item : cart) {
            sb.append(item.getItem().getName() + " x " + item.getQuantity() + "\n");
            total += item.getItem().getPrice() * item.getQuantity();
        }
        sb.append("\n");
        sb.append("Total: ₹" + total + "\n");
        cartTextArea.setText(sb.toString());
    }

    public static void main(String[] args) {

        String username = JOptionPane.showInputDialog(null, "Enter username:");
        String password = JOptionPane.showInputDialog(null, "Enter password:");

        String validUsername = "admin";
        String validPassword = "123";

        if (username != null && password != null && username.equals(validUsername) && password.equals(validPassword)) {
            // If valid then user able to show this code
            new CanteenManagementSystem();
        } else {
            // else if it is invalid then show error message
            JOptionPane.showMessageDialog(null, "Invalid username or password. Exiting program.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

    }

}
