						XpressCashDrawer
						================

Xpressosystems Cash Drawer Minimalistic App 4 little retail app

LOCAL building.

	mvn clean package -Pinstaller

RUN from here:

	mvn clean exec:java -Dexec.mainClass=com.xpressosystems.xpresscashdrawer.Main

Update & Upload the installer int the website

	mvn clean install -Pinstaller

