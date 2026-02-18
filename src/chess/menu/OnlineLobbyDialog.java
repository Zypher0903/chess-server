package chess.menu;

import chess.account.AccountManager;
import chess.gui.OnlineGameWindow;
import chess.online.OnlineClient;
import chess.pieces.Piece.PieceColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Shows a "Finding opponent..." dialog and connects to the online server.
 * Default server: chess-server host can be configured.
 */
public class OnlineLobbyDialog extends JDialog implements OnlineClient.MessageListener {

    // ──────────────────────────────────────────────────────────────────────────
    // Change SERVER_HOST to the IP / hostname of your ChessServer if self-hosted.
    // For local testing both players run on the same machine: use "localhost"
    private static final String DEFAULT_HOST = "localhost";
    private static final int    DEFAULT_PORT = 55201;
    // ──────────────────────────────────────────────────────────────────────────

    private final AccountManager accountManager;
    private final JFrame parent;
    private OnlineClient client;
    private JLabel statusLabel;
    private JLabel dotsLabel;
    private Timer animTimer;
    private int dots = 0;
    private boolean connected = false;

    private static final Color BG   = new Color(13, 17, 23);
    private static final Color CARD = new Color(22, 27, 34);
    private static final Color BLUE = new Color(88, 166, 255);
    private static final Color TEXT = new Color(230, 237, 243);
    private static final Color MUTED= new Color(125, 133, 144);

    public OnlineLobbyDialog(JFrame parent, AccountManager accountManager) {
        super(parent, "Online Multiplayer", true);
        this.parent = parent;
        this.accountManager = accountManager;
        setSize(420, 320);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { cancelAndClose(); }
        });
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(BG);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setLayout(null);

        JLabel title = new JLabel("Online Matchmaking");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(TEXT);
        title.setBounds(0, 30, 420, 32);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        root.add(title);

        // Server address input
        JLabel hostLbl = new JLabel("Server address:");
        hostLbl.setFont(new Font("Arial", Font.PLAIN, 12));
        hostLbl.setForeground(MUTED);
        hostLbl.setBounds(60, 82, 130, 22);
        root.add(hostLbl);

        JTextField hostField = new JTextField(DEFAULT_HOST);
        hostField.setBounds(60, 104, 220, 34);
        hostField.setBackground(new Color(33, 38, 45));
        hostField.setForeground(TEXT);
        hostField.setCaretColor(BLUE);
        hostField.setFont(new Font("Arial", Font.PLAIN, 14));
        hostField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(48, 54, 61)),
            BorderFactory.createEmptyBorder(2, 10, 2, 10)));
        root.add(hostField);

        JLabel portLbl = new JLabel("Port:");
        portLbl.setFont(new Font("Arial", Font.PLAIN, 12));
        portLbl.setForeground(MUTED);
        portLbl.setBounds(290, 82, 70, 22);
        root.add(portLbl);

        JTextField portField = new JTextField(String.valueOf(DEFAULT_PORT));
        portField.setBounds(290, 104, 80, 34);
        portField.setBackground(new Color(33, 38, 45));
        portField.setForeground(TEXT);
        portField.setCaretColor(BLUE);
        portField.setFont(new Font("Arial", Font.PLAIN, 14));
        portField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(48, 54, 61)),
            BorderFactory.createEmptyBorder(2, 10, 2, 10)));
        root.add(portField);

        // Status area (hidden until connecting)
        JPanel statusCard = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        statusCard.setLayout(null);
        statusCard.setOpaque(false);
        statusCard.setBounds(60, 155, 300, 90);
        statusCard.setVisible(false);
        root.add(statusCard);

        JLabel spinnerLbl = new JLabel("⟳");
        spinnerLbl.setFont(new Font("Serif", Font.PLAIN, 32));
        spinnerLbl.setForeground(BLUE);
        spinnerLbl.setBounds(0, 10, 300, 40);
        spinnerLbl.setHorizontalAlignment(SwingConstants.CENTER);
        statusCard.add(spinnerLbl);

        statusLabel = new JLabel("Connecting...");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 15));
        statusLabel.setForeground(TEXT);
        statusLabel.setBounds(0, 48, 300, 26);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusCard.add(statusLabel);

        dotsLabel = statusLabel; // reuse

        // Spinner animation
        animTimer = new Timer(600, e -> {
            dots = (dots + 1) % 4;
            String d = ".".repeat(dots);
            if (statusLabel.getText().startsWith("Finding")) statusLabel.setText("Finding opponent" + d);
        });

        // Connect button
        JButton connectBtn = new JButton("Find Opponent") {
            boolean hov = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hov=true; repaint(); }
                public void mouseExited(MouseEvent e)  { hov=false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hov ? BLUE.brighter() : BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        connectBtn.setBounds(60, 158, 300, 46);
        connectBtn.setFont(new Font("Arial", Font.BOLD, 15));
        connectBtn.setForeground(Color.WHITE);
        connectBtn.setFocusPainted(false);
        connectBtn.setBorderPainted(false);
        connectBtn.setContentAreaFilled(false);
        connectBtn.setOpaque(false);
        connectBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        root.add(connectBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(150, 226, 120, 36);
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 13));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBackground(new Color(52, 73, 94));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> cancelAndClose());
        root.add(cancelBtn);

        connectBtn.addActionListener(e -> {
            String host = hostField.getText().trim();
            int port;
            try { port = Integer.parseInt(portField.getText().trim()); }
            catch (NumberFormatException ex) { port = DEFAULT_PORT; }

            connectBtn.setVisible(false);
            hostField.setEnabled(false);
            portField.setEnabled(false);
            statusCard.setVisible(true);
            statusLabel.setText("Connecting...");
            animTimer.start();

            String username = accountManager.getCurrentAccount().getUsername();
            final String h = host;
            final int p = port;

            new Thread(() -> {
                client = new OnlineClient(this);
                boolean ok = client.connect(h, p, username);
                if (!ok) {
                    SwingUtilities.invokeLater(() -> {
                        animTimer.stop();
                        statusLabel.setText("Connection failed.");
                        connectBtn.setVisible(true);
                        hostField.setEnabled(true);
                        portField.setEnabled(true);
                        statusCard.setVisible(false);
                    });
                } else {
                    SwingUtilities.invokeLater(() -> statusLabel.setText("Finding opponent"));
                }
            }).start();
        });

        setContentPane(root);
    }

    // ─── OnlineClient.MessageListener ────────────────────────────────────────

    @Override
    public void onMessage(String msg) {
        SwingUtilities.invokeLater(() -> {
            if (msg.startsWith("GAME_START")) {
                String[] parts = msg.split(" ");
                PieceColor color = parts[1].equals("WHITE") ? PieceColor.WHITE : PieceColor.BLACK;
                String opponentName = parts[2];
                animTimer.stop();
                connected = true;
                // Open game window
                OnlineGameWindow gw = new OnlineGameWindow(accountManager, client, color, opponentName);
                gw.setVisible(true);
                dispose();
            } else if (msg.equals("WAITING")) {
                statusLabel.setText("Finding opponent");
            }
        });
    }

    @Override
    public void onDisconnected() {
        if (!connected) {
            SwingUtilities.invokeLater(() -> {
                animTimer.stop();
                statusLabel.setText("Disconnected.");
            });
        }
    }

    private void cancelAndClose() {
        animTimer.stop();
        if (client != null) client.disconnect();
        dispose();
    }
}
