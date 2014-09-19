package aScroll;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;


public class scrolledCompositeTest{
  public static void main (String [] args) {
    Display display = new Display ();
    Color red = display.getSystemColor(SWT.COLOR_RED);
    Color blue = display.getSystemColor(SWT.COLOR_BLUE);
    Shell shell = new Shell (display);
    shell.setLayout(new FillLayout());
      
    // set the size of the scrolled content - method 1
    final AScrolledComposite sc1 = new AScrolledComposite(shell,  SWT.NONE);
    final Composite c1 = new Composite(sc1, SWT.NONE);
    sc1.setContent(c1);
    c1.setBackground(red);
    GridLayout layout = new GridLayout();
    layout.numColumns = 4;
    c1.setLayout(layout);
    sc1.addPaintListener(new PaintListener() {
      
      @Override
      public void paintControl(PaintEvent e) { 
        System.out.println("scrollBound:"+sc1.getBounds());
      }
    });
    Button b1 = new Button (c1, SWT.PUSH);
    b1.setText("first button");
    c1.setSize(c1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    c1.addPaintListener(new PaintListener() {
      
      @Override
      public void paintControl(PaintEvent e) {
        System.out.println("border s1:"+sc1.getBorderWidth());
        System.out.println("c1 size" + c1.getSize());
        System.out.println("sc1 minheight:"+sc1.getMinHeight()+"minwidth:"+sc1.getMinWidth());
        System.out.println("sc1 size:"+sc1.getSize());
      }
    });
    sc1.setMinSize(10, 10);
    sc1.setMinWidth(1);
    // set the minimum width and height of the scrolled content - method 2
    final AScrolledComposite sc2 = new AScrolledComposite(shell,  SWT.BORDER);
    sc2.setExpandHorizontal(true);
    sc2.setExpandVertical(true);
    final Composite c2 = new Composite(sc2, SWT.NONE);
    sc2.setContent(c2);
    c2.setBackground(blue);
//    c2.setSize(c2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    layout = new GridLayout();
    layout.numColumns = 4;
    c2.setLayout(layout);
    Button b2 = new Button (c2, SWT.PUSH);
    b2.setText("first button");
    System.out.println("swt default value:"+SWT.DEFAULT);
    System.out.println("c2.computeSize result:"+c2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    System.out.println("border width:"+c2.getBorderWidth());
    System.out.println("c2 defaultBounds:"+c2.getBounds());
    sc2.setMinSize(200, 200);
//    sc2.setMinSize(10, 10);
    sc2.addPaintListener(new PaintListener() {
      
      @Override
      public void paintControl(PaintEvent e) {
        System.out.println("sc2 minSize:"+sc2.getSize());
        
      }
    });
    
    c2.addPaintListener(new PaintListener() {
      
      @Override
      public void paintControl(PaintEvent e) {
        System.out.println("c2 size:" + c2.getSize());
        System.out.println("sc2 minheight:"+sc2.getMinHeight()+"minwidth:"+sc2.getMinWidth());
        System.out.println("sc2 size:"+sc2.getSize());
      }
    });
    Button add = new Button (shell, SWT.PUSH);
    add.setText("add children");
    final int[] index = new int[]{0};
    add.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event e) {
            index[0]++;
            Button button = new Button(c1, SWT.PUSH);
            button.setText("button "+index[0]);
            // reset size of content so children can be seen - method 1
            c1.setSize(c1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
            c1.layout();
            
            button = new Button(c2, SWT.PUSH);
            button.setText("button "+index[0]);
            // reset the minimum width and height so children can be seen - method 2
      //      sc2.setMinSize(c2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
            c2.layout();
        }
    });

    shell.open ();
    while (!shell.isDisposed ()) {
        if (!display.readAndDispatch ()) display.sleep ();
    }
    display.dispose ();
}
}