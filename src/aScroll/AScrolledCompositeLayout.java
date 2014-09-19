package aScroll;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

public class AScrolledCompositeLayout extends Layout{

  @Override
  protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
    System.out.println("in my layout");
    
    return null;
  }

  @Override
  protected void layout(Composite composite, boolean flushCache) {
    System.out.println("in my layout layoutFunction;");
    AScrolledComposite asc = (AScrolledComposite)composite;
    if(asc.getContent() == null) return;
    AScrolledComposite.ScrollBar hBar = asc.getHBar();
    AScrolledComposite.ScrollBar vBar = asc.getVBar();
    
  }
  
}
