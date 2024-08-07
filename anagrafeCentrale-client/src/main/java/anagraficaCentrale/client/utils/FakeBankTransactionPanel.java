package anagraficaCentrale.client.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public abstract class FakeBankTransactionPanel {
	protected JFrame frame;
	protected JPanel panel;
	protected JLabel loadingLabel;
	protected JProgressBar progressBar;
    public FakeBankTransactionPanel() {
    	frame = new JFrame("Fake Bank Transaction");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.YELLOW);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        panel.add(progressBar, BorderLayout.NORTH);

        loadingLabel = new JLabel("Processing transaction...");
        panel.add(loadingLabel, BorderLayout.CENTER);

        frame.getContentPane().add(panel);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        new TransactionWorker().execute();
    }

    private class TransactionWorker extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            Thread.sleep(4000); // Simulate transaction
            panel.setBackground(Color.GREEN);
            loadingLabel.setText("Done!");
            progressBar.setVisible(false);
            Thread.sleep(1000);
            return null;
        }

        @Override
        protected void done() {
            try {
                get(); // Handle potential exceptions
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
                SwingUtilities.invokeLater(() -> {
                    // Close the frame on the EDT
                    frame.dispose();
                    callback();
                });
            }
        }
    }
    
    public abstract void callback();
}
