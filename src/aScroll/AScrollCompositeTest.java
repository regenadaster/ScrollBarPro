package aScroll;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class AScrollCompositeTest{
  public static void main(String[] args) {
    Display display = new Display();// 创建一个display对象。
    final Shell shell = new Shell(display);
    shell.setBounds(100, 100, 500, 500);
    final AScrolledComposite asc=new AScrolledComposite(shell, SWT.NONE);
    asc.setBounds(0, 0, 300, 300);
    Composite c=new Composite(asc, SWT.NONE);
    c.setBounds(0, 0, 600, 500);
    c.addControlListener(new ControlListener() {
      
      @Override
      public void controlResized(ControlEvent e) {
        
      }
      
      @Override
      public void controlMoved(ControlEvent e) {
        System.out.println("origin point:"+asc.getOrigin());
      }
    });
    asc.setContent(c);
    Button btn = new Button(c, SWT.NONE);
    btn.setText("hello");
    btn.setBounds(0, 0, 600, 500);
    btn.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
    Color backgroundColor = display.getSystemColor(SWT.COLOR_WHITE);
    shell.setBackground(backgroundColor);
    shell.open();
    while (!shell.isDisposed()) { // 如果主窗体没有关闭则一直循环
      if (!display.readAndDispatch()) { // 如果display不忙
        display.sleep(); // 休眠
      }
    }
    display.dispose();
  }
}
