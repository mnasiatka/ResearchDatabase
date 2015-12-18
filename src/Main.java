import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main extends JFrame implements ActionListener {

	private static final Insets WEST_INSETS = new Insets(5, 0, 5, 5);
	private static final Insets EAST_INSETS = new Insets(5, 5, 5, 0);
	private final String[] addLabels = new String[] { "Name", "Birthdate", "PhoneNumber", "Email",
			"Address", "DominantHand", "HearingLoss", "VisionImpairment", "FirstLanguage",
			"LearningDisability", "SpeechLanguageReadingDisorder", "NeurologicIllness",
			"StrokeAneurysmSevereHeartAttack", "PsychiatricMentalIllness", "IllicitDrugUse",
			"AlcoholAbuse", "Medications", "FutureStudies", "MRIEnvironmentBefore",
			"Claustrophobic", "ContactsGlasses", "MetalCannotRemove", "WeighLess250" };
	private final JTextField[] addFields = new JTextField[addLabels.length];
	private final ArrayList<JLabel> names = new ArrayList<JLabel>();

	JFrame frame;
	JPanel allSubjectsPanel, mainPanel, headerPanel;
	JPanel addSubjectPanel = new JPanel(new GridBagLayout());
	JPanel viewSubjectPanel = new JPanel(new GridBagLayout());
	JLabel searchLabel;
	JButton addSubjectButton, submit, cancel;
	JTextField searchField;
	JScrollPane scrollPane;

	QueryData mQuery;
	Dimension screenDimension;

	JLabel[] subjectValues = new JLabel[addLabels.length];

	int SELECTED_ID = -1;
	ArrayList<Participant> subjects = new ArrayList<Participant>();

	public static void main(String args[]) {
		new Main();
	}

	public Main() {
		new GetAllSubjectsTask().execute();
		setupFrame();
		setupTabs();
		setupPanels();
		frame.setVisible(true);
	}

	private void setupFrame() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenDimension);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLayout(new BorderLayout());
	}

	private void setupTabs() {
		headerPanel = new JPanel(new GridBagLayout());
		headerPanel.setBorder(BorderFactory.createEtchedBorder());

		searchLabel = new JLabel("Search ", JLabel.RIGHT);
		searchField = new JTextField(JTextField.RIGHT);
		Dimension d = searchField.getPreferredSize();
		d.width = screenDimension.width >> 2;
		searchField.setPreferredSize(d);

		GridBagConstraints c = new GridBagConstraints();

		addSubjectButton = new JButton("Add Subject");
		addSubjectButton.addActionListener(this);

		c = createGbc(0, 0);
		headerPanel.add(addSubjectButton, c);

		c = createGbc(1, 0);
		headerPanel.add(searchLabel, c);

		c = createGbc(2, 0);
		headerPanel.add(searchField, c);

		frame.add(headerPanel, BorderLayout.NORTH);
	}

	private void setupPanels() {
		mainPanel = new JPanel();
		setupViewAllSubjectsPanel();
		setupAddSubjectPanel();
		setupViewSubjectPanel();
	}

	private void setupViewSubjectPanel() {
		viewSubjectPanel = new JPanel(new GridBagLayout());
		viewSubjectPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("View Subject"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
	}

	private void setupAddSubjectPanel() {
		addSubjectPanel = new JPanel(new GridBagLayout());
		addSubjectPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Add Subject"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		GridBagConstraints c;
		int i = 0;
		for (i = 0; i < addLabels.length; i++) {
			addFields[i] = new JTextField();

			c = createGbc(0, i);
			addSubjectPanel.add(new JLabel(addLabels[i]), c);

			c = createGbc(1, i);
			addSubjectPanel.add(addFields[i], c);
		}
		c = createGbc(0, i);
		submit = new JButton("Submit");
		submit.addActionListener(this);
		addSubjectPanel.add(submit, c);

		c.gridx++;
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		addSubjectPanel.add(cancel, c);

		frame.add(addSubjectPanel, BorderLayout.CENTER);

	}

	private void setupViewAllSubjectsPanel() {
		allSubjectsPanel = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
		c.insets = new Insets(2, 5, 2, 5);
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;

		System.out.println(subjects.size());

		for (int i = 0; i < subjects.size(); i++) {
			final JLabel label = new JLabel(subjects.get(i).hm.get("Name").toString());
			final int position = i;
			label.setForeground(Color.BLACK);
			label.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					label.setForeground(Color.RED);
					if (SELECTED_ID != -1) {
						names.get(SELECTED_ID).setForeground(Color.BLACK);
					}
					SELECTED_ID = position;
					names.get(SELECTED_ID).setForeground(Color.RED);
					updateViewPanel();
				}

				@Override
				public void mousePressed(MouseEvent e) {

				}

				@Override
				public void mouseReleased(MouseEvent e) {

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					if (SELECTED_ID != position) {
						label.setForeground(Color.BLUE);
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					if (SELECTED_ID != position) {
						label.setForeground(Color.BLACK);
					}
				}

			});
			names.add(label);
			allSubjectsPanel.add(label, c);
			c.gridy++;
		}

		scrollPane = new JScrollPane(allSubjectsPanel);
		Dimension d = scrollPane.getPreferredSize();
		d.width = d.width << 1;
		scrollPane.setPreferredSize(d);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		allSubjectsPanel.setAutoscrolls(true);
		allSubjectsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		frame.add(scrollPane, BorderLayout.WEST);

	}

	private void clearMainPanel() {
		frame.remove(addSubjectPanel);
		frame.remove(viewSubjectPanel);
	}

	private void showAddPanel() {
		frame.invalidate();
		clearMainPanel();
		frame.add(addSubjectPanel);
		frame.repaint();
		frame.validate();
	}

	private void updateViewPanel() {
		frame.invalidate();
		clearMainPanel();
		viewSubjectPanel = new JPanel(new GridBagLayout());
		viewSubjectPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("View Subject"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		GridBagConstraints c;
		int i = 0;
		for (i = 0; i < subjectValues.length; i++) {
			subjectValues[i] = new JLabel(subjects.get(SELECTED_ID).hm.get(addLabels[i]));

			c = createGbc(0, i);
			viewSubjectPanel.add(new JLabel(addLabels[i] + ": "), c);

			c = createGbc(1, i);
			viewSubjectPanel.add(subjectValues[i], c);

		}

		frame.add(viewSubjectPanel, BorderLayout.CENTER);
		frame.repaint();
		frame.validate();
	}

	public void updateAllSubjectsPanel() {
		frame.invalidate();
		frame.remove(scrollPane);
		setupViewAllSubjectsPanel();
		frame.repaint();
		frame.validate();
	}

	private GridBagConstraints createGbc(int x, int y) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;

		gbc.anchor = (x == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;
		gbc.fill = (x == 0) ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;

		gbc.insets = (x == 0) ? WEST_INSETS : EAST_INSETS;
		gbc.weightx = (x == 0) ? 0.1 : 1.0;
		gbc.weighty = 1.0;
		return gbc;
	}

	private class AddSubjectTask extends SwingWorker<Boolean, String> {

		@Override
		protected Boolean doInBackground() throws Exception {
			String result = "", inputLine = "";
			HttpURLConnection conn = null;
			URL url;
			OutputStream out;
			BufferedReader in;
			JSONArray arr, data;
			result = "";
			try {
				// System.out.println(mQuery.getQuery());
				url = new URL("http://ythogh.com/wheelerlab/scripts/addSubject.php");
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("User-Agent", "Applet");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				conn.setRequestProperty("Content-Length", "" + mQuery.getQuery().length());
				out = conn.getOutputStream();
				out.write(mQuery.getQuery().getBytes());
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				inputLine = "";
				while ((inputLine = in.readLine()) != null) {
					result = result + inputLine;
					System.out.println(result);
				}
				conn.disconnect();
				out.close();
				in.close();

				if (result.contains("1")) {
					JOptionPane.showMessageDialog(frame, "Succesfully added new subject!",
							"Adding new subject", JOptionPane.PLAIN_MESSAGE);

					String[] values = new String[addFields.length];
					for (int i = 0; i < values.length; i++) {
						values[i] = addFields[i].getText().trim();
					}
					subjects.add(new Participant(values));
					updateAllSubjectsPanel();

					for (JTextField field : addFields) {
						field.setText("");
					}

					return true;
				}

				return false;
			} catch (Exception e) {
				e.printStackTrace();
				conn.disconnect();
				JOptionPane
						.showMessageDialog(
								frame,
								"Either this person has already been added or there were blank fields when you submitted",
								"Adding new subject", JOptionPane.PLAIN_MESSAGE);
			}
			return false;
		}

		@Override
		protected void done() {

		}

	}

	private class GetAllSubjectsTask extends SwingWorker<Boolean, String> {

		@Override
		protected Boolean doInBackground() throws Exception {
			String result = "", inputLine = "";
			HttpURLConnection conn = null;
			URL url;
			OutputStream out;
			BufferedReader in;
			JSONArray arr;
			JSONObject obj;
			mQuery = new QueryData();
			mQuery.add("username", "username");
			result = "";
			try {
				url = new URL("http://ythogh.com/wheelerlab/scripts/getSubjects.php");
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("User-Agent", "Applet");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				conn.setRequestProperty("Content-Length", "" + mQuery.getQuery().length());
				out = conn.getOutputStream();
				out.write(mQuery.getQuery().getBytes());
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				inputLine = "";
				while ((inputLine = in.readLine()) != null) {
					result = result + inputLine;
					// System.out.println(result);
				}
				int index = 0;
				arr = new JSONArray(result);
				String values[];
				// System.out.println(arr.length());
				for (int i = 0; i < arr.length(); i++) {
					obj = arr.getJSONObject(i);
					values = new String[addLabels.length];
					for (int j = 0; j < values.length; j++) {
						values[j] = obj.getString(addLabels[j]);
					}
					subjects.add(new Participant(values));
				}
				conn.disconnect();
				out.close();
				in.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				conn.disconnect();
			}
			return false;
		}

		@Override
		protected void done() {
			updateAllSubjectsPanel();
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == submit) {

			mQuery = new QueryData();
			for (int i = 0; i < addFields.length; i++) {
				mQuery.add(addLabels[i], addFields[i].getText());
			}
			try {
				new AddSubjectTask().execute();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		} else if (e.getSource() == cancel) {
			for (JTextField field : addFields) {
				field.setText("");
			}
		} else if (e.getSource() == addSubjectButton) {
			showAddPanel();
		}
	}

}
