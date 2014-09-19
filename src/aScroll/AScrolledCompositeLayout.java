package aScroll;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

public class AScrolledCompositeLayout extends Layout{
  
  static final int DEFAULT_WIDTH = 64;
  static final int DEFAULT_HEIGHT = 64;
  boolean inLayout = false;
  
  
  @Override
  protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
    System.out.println("in my layout");
    
    return null;
  }

  @Override
  protected void layout(Composite composite, boolean flushCache) {
    System.out.println("in my layout layoutFunction;");
    if(inLayout) return;
    AScrolledComposite asc = (AScrolledComposite)composite;
    if(asc.getContent() == null) return;
    AScrolledComposite.ScrollBar hBar = asc.getHBar();
    AScrolledComposite.ScrollBar vBar = asc.getVBar();
    if (hBar != null) {
      if (hBar.getSize().y >= asc.getSize().y) {
        return;
      }
    }
    if (vBar != null) {
      if (vBar.getSize().x >= asc.getSize().x) {
        return;
      }
    }
    inLayout = true;
    Rectangle hostRect = asc.getClientArea();
    Rectangle contentRect = asc.getContent().getBounds();
    asc.setVerticalBar();
    asc.setHorizontalBar();
    if (asc.getExpandHorizontal()) {
      contentRect.width = Math.max(asc.getMinWidth(), hostRect.width);  
    }
    if (asc.getExpandVertical()) {
      contentRect.height = Math.max(asc.getMinHeight(), hostRect.height);
    }
    asc.getContent().setBounds (contentRect);
    asc.updateScrolledComposite();
    inLayout = false;
  }
  
}
