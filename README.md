Intellij IDEA JCR content editor plugin
=======================================

The idea behind this plugin is to allow people developing for CQ5 to use intellij for all aspects of development, so
that they don't need to utilize CRXDE to edit jcr content (nodes/properties).

Intended development process using this plugin
----------------------------------------------

1.	Use vault to export the JCR tree to a folder in your project directory
2.	Set up your connection settings under project settings
3.	Unpack any xml files exported by vault that you wish to edit, other than .content.xml (right click on xml file and select unpack xml)
4.	Delete the original xml file if everything looks ok (eventually this will be done automatically in the unpack step, when this plugin is more mature/battle-tested)
5.	Right click on files/folders in the content tree to add/edit nodes.
6.	Use vault to import your new content back into CRX (I recommend using slantedjavas setup: http://slantedjava.blogspot.com/2011/03/using-vault-from-intellij.html). You should be able to import unpacked content

Setup
-----

Go to project settings -> JCR Connection Settings 
You may need to close/reopen project for changes to take effect.
Currently the plugin only uses the JCR to build node definitions.


Functions
---------
Two new actions are added to the new menu when you right click in the project
pane: "New Component" and "New Node". New Component is just New Node where the
nodetype is already set to cq:Component. 

Right clicking on a .content.xml file, you can select "Edit Node" to bring up
a dialog for editing node properties. 

If you wish to edit content exported by vlt, you can right-click on an xml file
(anything created by vlt like dialog.xml, except .content.xml which essentially
is already unpacked), and select unpack xml. This creates folders and
.content.xml files for the inner nodes of the xml file. After doing so, you 
will probably want to delete the original xml file.

