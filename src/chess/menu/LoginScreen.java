package chess.menu;

import chess.account.AccountManager;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class LoginScreen extends JFrame {
    private AccountManager accountManager;
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JTextField signupUsernameField;
    private JPasswordField signupPasswordField;
    private JPasswordField signupConfirmPasswordField;
    private JPanel loginTab;
    private JPanel signupTab;
    private JButton loginTabBtn;
    private JButton signupTabBtn;

    private static final Color BG_DARK      = new Color(13, 17, 23);
    private static final Color BG_CARD      = new Color(22, 27, 34);
    private static final Color BG_FIELD     = new Color(33, 38, 45);
    private static final Color ACCENT_BLUE  = new Color(88, 166, 255);
    private static final Color ACCENT_GREEN = new Color(63, 185, 80);
    private static final Color BORDER_COLOR = new Color(48, 54, 61);
    private static final Color TEXT_PRIMARY = new Color(230, 237, 243);
    private static final Color TEXT_MUTED   = new Color(125, 133, 144);
    private static final Color ERROR_RED    = new Color(248, 81, 73);

    public LoginScreen(AccountManager accountManager) {
        this.accountManager = accountManager;
        setTitle("Chess Pro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 620);
        setResizable(false);
        setLocationRelativeTo(null);
        initializeUI();
    }

    private void initializeUI() {
        JPanel root = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_DARK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                RadialGradientPaint glow = new RadialGradientPaint(
                    240, 0, 300,
                    new float[]{0f, 1f},
                    new Color[]{new Color(88, 166, 255, 35), new Color(0,0,0,0)}
                );
                g2.setPaint(glow);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setLayout(null);

        JLabel icon = new JLabel("♛");
        icon.setFont(new Font("Serif", Font.PLAIN, 60));
        icon.setForeground(ACCENT_BLUE);
        icon.setBounds(0, 28, 480, 70);
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        root.add(icon);

        JLabel title = new JLabel("Chess Pro");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(TEXT_PRIMARY);
        title.setBounds(0, 96, 480, 44);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        root.add(title);

        JLabel sub = new JLabel("Play Chess with Anyone in the World");
        sub.setFont(new Font("Arial", Font.PLAIN, 14));
        sub.setForeground(TEXT_MUTED);
        sub.setBounds(0, 140, 480, 24);
        sub.setHorizontalAlignment(SwingConstants.CENTER);
        root.add(sub);

        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
            }
        };
        card.setLayout(null);
        card.setOpaque(false);
        card.setBounds(40, 178, 400, 400);
        root.add(card);

        loginTabBtn = makeTabBtn("Sign In");
        loginTabBtn.setBounds(0, 0, 200, 48);
        card.add(loginTabBtn);
        loginTabBtn.addActionListener(e -> setActiveTab(0));

        signupTabBtn = makeTabBtn("Create Account");
        signupTabBtn.setBounds(200, 0, 200, 48);
        card.add(signupTabBtn);
        signupTabBtn.addActionListener(e -> setActiveTab(1));

        JPanel sep = new JPanel();
        sep.setBackground(BORDER_COLOR);
        sep.setBounds(0, 48, 400, 1);
        card.add(sep);

        loginTab = buildLoginPanel();
        loginTab.setBounds(0, 49, 400, 350);
        card.add(loginTab);

        signupTab = buildSignupPanel();
        signupTab.setBounds(0, 49, 400, 350);
        signupTab.setVisible(false);
        card.add(signupTab);

        setContentPane(root);
        setActiveTab(0);
    }

    private JButton makeTabBtn(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
                if (Boolean.TRUE.equals(getClientProperty("active"))) {
                    g2.setColor(ACCENT_BLUE);
                    g2.setStroke(new BasicStroke(2.5f));
                    g2.drawLine(20, getHeight()-2, getWidth()-20, getHeight()-2);
                }
            }
        };
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(TEXT_MUTED);
        btn.setOpaque(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void setActiveTab(int tab) {
        loginTab.setVisible(tab == 0);
        signupTab.setVisible(tab == 1);
        loginTabBtn.setForeground(tab == 0 ? TEXT_PRIMARY : TEXT_MUTED);
        signupTabBtn.setForeground(tab == 1 ? TEXT_PRIMARY : TEXT_MUTED);
        loginTabBtn.putClientProperty("active", tab == 0);
        signupTabBtn.putClientProperty("active", tab == 1);
        loginTabBtn.repaint();
        signupTabBtn.repaint();
        if (tab == 0 && loginUsernameField != null) loginUsernameField.requestFocus();
        else if (tab == 1 && signupUsernameField != null) signupUsernameField.requestFocus();
    }

    private JPanel buildLoginPanel() {
        JPanel p = new JPanel(null);
        p.setOpaque(false);
        addLabel(p, "Username", 30, 22);
        loginUsernameField = makeTextField(); loginUsernameField.setBounds(30, 44, 340, 44); p.add(loginUsernameField);
        addLabel(p, "Password", 30, 104);
        loginPasswordField = makePassField(); loginPasswordField.setBounds(30, 126, 340, 44); p.add(loginPasswordField);
        JButton btn = makeBtn("Sign In", ACCENT_BLUE); btn.setBounds(30, 192, 340, 48);
        btn.addActionListener(e -> handleLogin()); p.add(btn);
        loginUsernameField.addActionListener(e -> loginPasswordField.requestFocus());
        loginPasswordField.addActionListener(e -> handleLogin());
        return p;
    }

    private JPanel buildSignupPanel() {
        JPanel p = new JPanel(null);
        p.setOpaque(false);
        addLabel(p, "Username (min. 3 chars)", 30, 10);
        signupUsernameField = makeTextField(); signupUsernameField.setBounds(30, 32, 340, 44); p.add(signupUsernameField);
        addLabel(p, "Password (min. 6 chars)", 30, 90);
        signupPasswordField = makePassField(); signupPasswordField.setBounds(30, 112, 340, 44); p.add(signupPasswordField);
        addLabel(p, "Confirm Password", 30, 170);
        signupConfirmPasswordField = makePassField(); signupConfirmPasswordField.setBounds(30, 192, 340, 44); p.add(signupConfirmPasswordField);
        JButton btn = makeBtn("Create Account", ACCENT_GREEN); btn.setBounds(30, 258, 340, 48);
        btn.addActionListener(e -> handleSignup()); p.add(btn);
        signupUsernameField.addActionListener(e -> signupPasswordField.requestFocus());
        signupPasswordField.addActionListener(e -> signupConfirmPasswordField.requestFocus());
        signupConfirmPasswordField.addActionListener(e -> handleSignup());
        return p;
    }

    private void addLabel(JPanel p, String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 11));
        l.setForeground(TEXT_MUTED);
        l.setBounds(x, y, 340, 18);
        p.add(l);
    }

    private JTextField makeTextField() {
        JTextField f = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_FIELD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(isFocusOwner() ? ACCENT_BLUE : BORDER_COLOR);
                g2.setStroke(new BasicStroke(isFocusOwner() ? 1.5f : 1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                super.paintComponent(g);
            }
        };
        f.setFont(new Font("Arial", Font.PLAIN, 15));
        f.setForeground(TEXT_PRIMARY); f.setCaretColor(ACCENT_BLUE);
        f.setOpaque(false); f.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { f.repaint(); }
            public void focusLost(FocusEvent e)   { f.repaint(); }
        });
        return f;
    }

    private JPasswordField makePassField() {
        JPasswordField f = new JPasswordField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_FIELD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(isFocusOwner() ? ACCENT_BLUE : BORDER_COLOR);
                g2.setStroke(new BasicStroke(isFocusOwner() ? 1.5f : 1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                super.paintComponent(g);
            }
        };
        f.setFont(new Font("Arial", Font.PLAIN, 15));
        f.setForeground(TEXT_PRIMARY); f.setCaretColor(ACCENT_BLUE);
        f.setOpaque(false); f.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { f.repaint(); }
            public void focusLost(FocusEvent e)   { f.repaint(); }
        });
        return f;
    }

    private JButton makeBtn(String text, Color color) {
        JButton btn = new JButton(text) {
            boolean hov = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hov=true; repaint(); }
                public void mouseExited(MouseEvent e)  { hov=false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hov ? color.brighter() : color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setContentAreaFilled(false); btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void handleLogin() {
        String u = loginUsernameField.getText().trim();
        String p = new String(loginPasswordField.getPassword());
        if (u.isEmpty() || p.isEmpty()) { showBanner("Please enter username and password.", ERROR_RED); return; }
        if (accountManager.login(u, p)) { openMainMenu(); }
        else { showBanner("Invalid username or password.", ERROR_RED); loginPasswordField.setText(""); loginPasswordField.requestFocus(); }
    }

    private void handleSignup() {
        String u = signupUsernameField.getText().trim();
        String p = new String(signupPasswordField.getPassword());
        String c = new String(signupConfirmPasswordField.getPassword());
        if (u.isEmpty() || p.isEmpty()) { showBanner("Please fill in all fields.", ERROR_RED); return; }
        if (u.length() < 3) { showBanner("Username must be at least 3 characters.", ERROR_RED); return; }
        if (p.length() < 6) { showBanner("Password must be at least 6 characters.", ERROR_RED); return; }
        if (!p.equals(c)) { showBanner("Passwords do not match.", ERROR_RED); signupConfirmPasswordField.setText(""); return; }
        if (accountManager.createAccount(u, p)) {
            showBanner("Account created! You can now sign in.", ACCENT_GREEN);
            signupUsernameField.setText(""); signupPasswordField.setText(""); signupConfirmPasswordField.setText("");
            Timer t = new Timer(1500, e -> setActiveTab(0)); t.setRepeats(false); t.start();
        } else { showBanner("Username already taken.", ERROR_RED); }
    }

    private void showBanner(String msg, Color color) {
        JWindow banner = new JWindow(this);
        JPanel bp = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        bp.setLayout(new BorderLayout());
        bp.setOpaque(false);
        JLabel lbl = new JLabel(msg);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(Color.WHITE);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        bp.add(lbl);
        banner.setContentPane(bp);
        banner.pack();
        Point loc = getLocation();
        banner.setLocation(loc.x + (getWidth() - banner.getWidth()) / 2, loc.y + 16);
        banner.setVisible(true);
        Timer t = new Timer(2500, e -> banner.dispose()); t.setRepeats(false); t.start();
    }

    private void openMainMenu() {
        MainMenu mm = new MainMenu(accountManager);
        mm.setVisible(true);
        dispose();
    }
}
