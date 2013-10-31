package velir.intellij.cq5.config;

import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.GuiUtils;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class JCRMountPointSettings {
	private class MountPointPanel {
		private JPanel jPanel;
		private JTextField fileSystemMountPoint;
		private JTextField jcrMountPoint;
		private JCRMountPoint initialValue;

		public MountPointPanel(JCRMountPoint jcrMountPoint, final JPanel parentPanel,
		                       final java.util.List<MountPointPanel> panelList) {
			initialValue = jcrMountPoint;

			jPanel = new JPanel(new FlowLayout());

			// filesystem mountpoint label
			jPanel.add(new JLabel("file system"));

			// filesystem mountpoint browse field
			fileSystemMountPoint = new JTextField(jcrMountPoint.getFileSystemMountPoint(), 15);
			jPanel.add(GuiUtils.constructDirectoryBrowserField(fileSystemMountPoint,
					"a filesystem mount point for JCR content"));

			// jcr mountpoint label
			jPanel.add(new JLabel("JCR"));

			// jcr mountpoint field
			this.jcrMountPoint = new JTextField(jcrMountPoint.getJcrNode(), 15);
			jPanel.add(this.jcrMountPoint);

			// remove button
			JButton jButton = new JButton("X");
			final MountPointPanel thisPanel = this;
			jButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					panelList.remove(thisPanel);
					parentPanel.remove(thisPanel.getjPanel());
					parentPanel.revalidate();
				}
			});
			jPanel.add(jButton);
		}

		public JCRMountPoint getJCRMountPoint() {
			return new JCRMountPoint(fileSystemMountPoint.getText(), jcrMountPoint.getText());
		}

		public void setJCRMountPoint(JCRMountPoint jcrMountPoint) {
			fileSystemMountPoint.setText(jcrMountPoint.getFileSystemMountPoint());
			this.jcrMountPoint.setText(jcrMountPoint.getJcrNode());
		}

		public JPanel getjPanel () {
			return jPanel;
		}

		public boolean isModified () {
			return initialValue.equals(getJCRMountPoint());
		}
	}

	private JPanel jPanel;
	private JPanel mountPointListPanel;
	private java.util.List<MountPointPanel> mountPointPanelList;
	private java.util.List<JCRMountPoint> initialValues;

	public JCRMountPointSettings (java.util.List<JCRMountPoint> jcrMountPointList) {
		initialValues = jcrMountPointList;

		jPanel = new JPanel(new VerticalFlowLayout());
		mountPointPanelList = new LinkedList<MountPointPanel>();

		// make panel for each existing mount point
		mountPointListPanel = new JPanel(new VerticalFlowLayout());
		for (JCRMountPoint jcrMountPoint : jcrMountPointList) {
			addMountPoint(jcrMountPoint);
		}

		// panel and scroll pane for list of mountpoints
		JBScrollPane jbScrollPane = new JBScrollPane(mountPointListPanel,
				JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JBScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jbScrollPane.setPreferredSize(new Dimension(300, 300));
		jPanel.add(jbScrollPane);

		JButton jButton = new JButton("add");
		jButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addMountPoint(new JCRMountPoint());
				jPanel.revalidate();
			}
		});
		jPanel.add(jButton);

	}

	private void addMountPoint (JCRMountPoint jcrMountPoint) {
		MountPointPanel mountPointPanel = new MountPointPanel(jcrMountPoint, mountPointListPanel, mountPointPanelList);
		mountPointPanelList.add(mountPointPanel);
		mountPointListPanel.add(mountPointPanel.getjPanel());
	}

	public java.util.List<JCRMountPoint> getMountPoints () {
		java.util.List<JCRMountPoint> mountPointList = new LinkedList<JCRMountPoint>();
		for (MountPointPanel mountPointPanel : mountPointPanelList) {
			mountPointList.add(mountPointPanel.getJCRMountPoint());
		}
		return mountPointList;
	}

	public JComponent createComponent() {
		return jPanel;
	}

	public boolean isModified () {
		boolean modified = false;
		List<JCRMountPoint> myMountPoints = getMountPoints();
		if (myMountPoints.size() != initialValues.size()) return true;
		for (int i = 0; i < myMountPoints.size(); i++) {
			modified = modified || ! myMountPoints.get(i).equals(initialValues.get(i));
		}

		return modified;
	}

	public void reset () {
		mountPointPanelList = new LinkedList<MountPointPanel>();
		mountPointListPanel.removeAll();
		for (JCRMountPoint jcrMountPoint : initialValues) {
			addMountPoint(jcrMountPoint);
		}
		mountPointListPanel.revalidate();
	}

}
