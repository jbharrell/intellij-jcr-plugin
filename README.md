Intellij IDEA JCR content editor plugin
=======================================

The idea behind this plugin is to allow people developing for CQ5 to use intellij for all aspects of development, so
that they don't need to utilize CRXDE to edit jcr content (nodes/properties).

Intended development process using this plugin
----------------------------------------------

1. export jcr tree using vlt
2. use plugin to "unpack" any existing xml specifying jcr nodes that you wish to edit (right click on xml file and
select unpack xml)
3. use vlt to import content back into the JCR (you should be able to import unpacked content)

Setup
-----

Go to project settings -> JCR Connection Settings 
You currently need to close/reopen project for changes to take effect, I think.
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

