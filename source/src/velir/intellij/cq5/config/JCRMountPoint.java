package velir.intellij.cq5.config;

public class JCRMountPoint {
	private static final String DEFAULT_FILESYSTEM_MOUNT_POINT = "";
	private static final String DEFAULT_JCR_MOUNT_POINT = "/apps";

	public String fileSystemMountPoint;
	public String jcrNode;

	public JCRMountPoint () {
		this(DEFAULT_FILESYSTEM_MOUNT_POINT, DEFAULT_JCR_MOUNT_POINT);
	}

	public JCRMountPoint (JCRMountPoint other) {
		fileSystemMountPoint = other.getFileSystemMountPoint();
		jcrNode = other.getJcrNode();
	}

	public JCRMountPoint (String fileSystemMountPoint, String jcrNode) {
		this.fileSystemMountPoint = fileSystemMountPoint;
		this.jcrNode = jcrNode;
	}

	public String getFileSystemMountPoint() {
		return fileSystemMountPoint;
	}

	public String getJcrNode() {
		return jcrNode;
	}

	public void setFileSystemMountPoint(String fileSystemMountPoint) {
		this.fileSystemMountPoint = fileSystemMountPoint;
	}

	public void setJcrNode(String jcrNode) {
		this.jcrNode = jcrNode;
	}

	public boolean equals (Object o) {
		if (! (o instanceof JCRMountPoint)) return false;

		JCRMountPoint other = (JCRMountPoint) o;
		return other.fileSystemMountPoint.equals(fileSystemMountPoint)
				&& other.jcrNode.equals(jcrNode);
	}
}
