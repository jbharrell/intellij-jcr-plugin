package velir.intellij.cq5.ui.window;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.TimerListener;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.table.JBTable;
import org.jdom.JDOMException;
import org.jetbrains.annotations.Nullable;
import velir.intellij.cq5.jcr.model.VNode;
import velir.intellij.cq5.util.PsiUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * @author krasmussen
 */
public class PropertyToolWindow implements ToolWindowFactory {
    private Project myProject;
    private VirtualFile myFile;
    private JBTable propertyTable;
    private Component myToolWindowContent;
    private ToolWindow toolWindow;

    public PropertyToolWindow(){

    }

    public void createToolWindowContent(Project project, ToolWindow window){

        myProject = project;
        toolWindow = window;

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        JScrollPane scrollPane = new JBScrollPane();

        Content content = contentFactory.createContent(scrollPane, "", false);
        ContentManager cnt = toolWindow.getContentManager();
        cnt.addContent(content);
        myToolWindowContent = scrollPane;

        ActionManager.getInstance().addTimerListener(500, new TimerListener() {
            public ModalityState getModalityState() {
                return ModalityState.stateForComponent(myToolWindowContent);
            }

            public void run() {
                checkUpdate();
            }
        });
    }

    private void checkUpdate() {
        if (myProject.isDisposed()) return;

        final Component owner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        if (SwingUtilities.isDescendingFrom(myToolWindowContent, owner) || JBPopupFactory.getInstance().isPopupActive()) return;

        final DataContext dataContext = DataManager.getInstance().getDataContext(owner);
        if (dataContext.getData("CQ Node Properties") == this) return;
        if (PlatformDataKeys.PROJECT.getData(dataContext) != myProject) return;

        final VirtualFile[] files = PlatformDataKeys.VIRTUAL_FILE_ARRAY.getData(dataContext);
        if (files != null && files.length == 1) {
            setFile(files[0]);
        }
        else if (files != null && files.length > 1) {
            setFile(null);
        }
    }

    private void setFile(@Nullable VirtualFile file) {
        if(myFile != file){
            myFile = file;
            ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
            ContentManager cnt = toolWindow.getContentManager();
            if(null == file){
                //If the user selects something that isn't a node, clear the table
                JScrollPane scrollPane = new JBScrollPane();

                Content content = contentFactory.createContent(scrollPane, "", false);
                cnt.removeAllContents(true);
                cnt.addContent(content);
                myToolWindowContent = scrollPane;
            }
            else if(file.isDirectory()){
                VirtualFile child = file.findChild(PsiUtils.CONTENT_XML);
                setFile(child);
            } else if(PsiUtils.CONTENT_XML.equals(file.getName())){
                try {
                    PsiFile psiFile = PsiManager.getInstance(myProject).findFile(file);
                    String name = PsiUtils.unmungeNamespace(psiFile.getContainingDirectory().getName());

                    VNode vNode = VNode.makeVNode(file.getInputStream(), name);

                    cnt.removeAllContents(true);

                    NodeTableModel model = new NodeTableModel(vNode);
                    propertyTable = new JBTable(model);

                    JScrollPane scrollPane = new JBScrollPane(propertyTable);
                    propertyTable.setFillsViewportHeight(true);
                    Content content = contentFactory.createContent(scrollPane, "", false);

                    cnt.addContent(content);
                    myToolWindowContent = scrollPane;
                } catch (JDOMException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else{
                //User selected a file that doesn't have CQ properties, clear the window
                setFile(null);
            }
        }
    }
}
