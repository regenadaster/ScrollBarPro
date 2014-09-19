package aScroll;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

public class AScrolledComposite extends Composite {
  private ScrollBar horizontalBar;
  private ScrollBar verticalBar;
  // complement in two bar's split;
  private Composite complement;

  private Control content = null;
  private Rectangle contentRect;
  private Rectangle bodyRect;
  private int defautBarVale = 15;

  private int minHeight;
  private int minWidth;
  private boolean expandHorizontal;
  private boolean expandVertical;
  
  public AScrolledComposite(Composite parent, int style) {
    super(parent, style);
    super.setLayout(new AScrolledCompositeLayout());
    initHorizontalBar();
    initVerticalBar();
    initComplement();
    parent.addControlListener(new ControlListener() {
      
      @Override
      public void controlResized(ControlEvent e) {
        updateScrolledComposite();
      }
      
      @Override
      public void controlMoved(ControlEvent e) {
       
        
      }
    });
  }

  private void initHorizontalBar() {
    horizontalBar = new ScrollBar(this, 1);
    horizontalBar.sethorizontal();
    horizontalBar.addPaintListener(new PaintListener() {

      @Override
      public void paintControl(PaintEvent e) {
        setHorizontalBar();
      }
    });
  }
  
  public ScrollBar getHBar(){
    return horizontalBar;
  }
  
  public ScrollBar getVBar(){
    return verticalBar;
  }
  
  public boolean isContainContent() {
    if (content != null || !content.isDisposed()) {
      return true;
    }
    return false;
  }

  public void setHorizontalBar() {
    updateBody();
    updateContent();
    System.out.println("setHorizontalBar");
    int bodyHeightValue = bodyRect.height - 2 * getBorderWidth();
    int bodyWidthValue = bodyRect.width - 2 * getBorderWidth();
    if (isContainContent()) {
      if (bodyWidthValue > contentRect.width) {
        horizontalBar.setVisible(false);
        horizontalBar.setBounds(new Rectangle(0, 0, 0, 0));
      } else {
        horizontalBar.setVisible(true);
        horizontalBar.setBounds(0, bodyHeightValue - defautBarVale, bodyWidthValue - defautBarVale, defautBarVale);
      }
    } else {
      horizontalBar.setBounds(new Rectangle(0, 0, 0, 0));
    }
    System.out.println("horizontalBar:" + horizontalBar.getBounds());
  }

  private void initVerticalBar() {
    verticalBar = new ScrollBar(this, 1);
    verticalBar.addSelectionListener(new SelectionListener() {

      @Override
      public void widgetSelected(SelectionEvent e) {

      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e) {

      }
    });
    verticalBar.addPaintListener(new PaintListener() {

      @Override
      public void paintControl(PaintEvent e) {
        setVerticalBar();
      }
    });
  }

  public void setVerticalBar() {
    updateBody();
    updateContent();
    System.out.println("setVerticalBar");
    int bodyHeightValue = bodyRect.height - getBorderWidth() * 2;
    int bodyWidthValue = bodyRect.width - getBorderWidth() * 2;
    System.out.println("contentRect.height:"+contentRect.height);
    System.out.println("containContent is:"+isContainContent());
    if (isContainContent()) {
      if (bodyHeightValue > contentRect.height) {
        verticalBar.setVisible(false);
        verticalBar.setBounds(new Rectangle(0, 0, 0, 0));
      } else {
        verticalBar.setVisible(true);
        verticalBar.setBounds(bodyWidthValue - defautBarVale, 0, defautBarVale, bodyHeightValue - defautBarVale);
      }
    }
    else{
      verticalBar.setBounds(new Rectangle(0, 0, 0, 0));
    }
    System.out.println("verticalBar:" + verticalBar.getBounds());
    System.out.println("isVerticalBar: visible?" + verticalBar.getIsVisible());
  }

  private void initComplement() {
    complement = new Composite(this, SWT.NONE);
    complement.redraw();
    complement.addPaintListener(new PaintListener() {

      @Override
      public void paintControl(PaintEvent e) {
        e.gc.fillRectangle(0, 0, complement.getBounds().width, complement.getBounds().height);
      }
    });
  }

  public int getMinHeight() {
    return minHeight;
  }
  
  public int getMinWidth() {
    return minWidth;
  }
  
  protected void updateComplement() {
    int bodyHeightValue = bodyRect.height - 2 * getBorderWidth();
    int bodyWidthValue = bodyRect.width - 2 * getBorderWidth();
    complement.setBounds(bodyWidthValue - defautBarVale, bodyHeightValue - defautBarVale, defautBarVale, defautBarVale);
    complement.redraw();
  }
  public void setExpandHorizontal(boolean expand) {
    checkWidget();
    if (expand == expandHorizontal) return;
    expandHorizontal = expand;
    layout(false);
  }
  
  public void setExpandVertical(boolean expand) {
    checkWidget();
    if (expand == expandVertical) return;
    expandVertical = expand;
    layout(false);
  }

  protected void updateContent() {
    contentRect = content.getBounds();
    contentRect.height = content.getSize().y;
  }

  public void setMinSize(Point size) {
    if (size == null) {
      setMinSize(0, 0);
    } else {
      setMinSize(size.x, size.y);
    }
  }

  public void setMinWidth(int width) {
    setMinSize(width, minHeight);
  }

  public void setMinSize(int width, int height) {
    checkWidget();
    if (width == minWidth && height == minHeight) return;
    minWidth = Math.max(0, width);
    minHeight = Math.max(0, height);
    layout(false);
  }
  
  public void setLayout (Layout layout) {
    checkWidget();
    return;
  }
  
  protected void updateBody() {
    bodyRect = getBounds();
  }

  public void setContentData() {
    updateBody();
    updateContent();
    double tmpVPercentage = getPercentage(verticalBar);
    double tmpHPercentage = getPercentage(horizontalBar);
    content.setLocation((int) (-contentRect.width * tmpHPercentage), (int) (-contentRect.height * tmpVPercentage));
  }

  public double getPercentage(ScrollBar sb) {
    return (double) sb.getSelection() / (double) (sb.getMaximum() - sb.getMinimum());
  }

  public void setScrollBarAttribute(ScrollBar sb){
    updateBody();
    updateContent();
    int bodyValue, contentValue;
    if (sb.getIsVertical()) { 
      bodyValue = bodyRect.height - defautBarVale;
      contentValue = contentRect.height;
    } else {
      bodyValue = bodyRect.width - defautBarVale;
      contentValue = contentRect.width;
    }
    int tmpMax = bodyValue - 3 * defautBarVale;
    sb.setMaximum(tmpMax);
    sb.setMinimum(0);
    double scales = (double) (sb.getMaximum() - sb.getMinimum());
    double percent = (double) bodyValue / (double) contentValue;
    int tmpThumb = (int) (scales * percent);
    sb.setThumb(tmpThumb);
    sb.setPageIncrement(tmpThumb);
  }
  
  
  public void setScrollBar(final ScrollBar sb) {
    setScrollBarAttribute(sb);
    sb.addSelectionListener(new SelectionListener() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        updateScrolledComposite();
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e) {

      }
    });
  }

  private void updateScrolledComposite(){
    updateComplement();
    setScrollBarAttribute(horizontalBar);
    setScrollBarAttribute(verticalBar);
    setContentData();
    setVerticalBar();
    setHorizontalBar();
  }
  
  public void setContent(Composite c) {
    content = c;
    setScrollBar(verticalBar);
    setScrollBar(horizontalBar);
    content.addPaintListener(new PaintListener() {
      
      @Override
      public void paintControl(PaintEvent e) {
        updateScrolledComposite();
        System.out.println("content painting");
      }
    });
    content.addControlListener(new ControlListener() {
      
      @Override
      public void controlResized(ControlEvent e) {
        System.out.println("control Resize!!");
        updateScrolledComposite();
        layout(false);
      }
      
      @Override
      public void controlMoved(ControlEvent e) {
        
      }
    });
  }

  public Control getContent() {
    return content;
  }

  public Point getOrigin() {
    if (content == null)
      return new Point(0, 0);
    Point location = content.getLocation();
    return new Point(-location.x, -location.y);
  }

  public void setOrigin(Point origin) {
    setOrigin(origin.x, origin.y);
  }

  public void setOrigin(int x, int y) {
    checkWidget();
    if (content == null)
      return;
    if (horizontalBar != null) {
      horizontalBar.setSelection(x);
      x = -horizontalBar.getSelection();
    } else {
      x = 0;
    }
    if (horizontalBar != null) {
      horizontalBar.setSelection(y);
      y = -horizontalBar.getSelection();
    } else {
      y = 0;
    }
    content.setLocation(x, y);
  }

  public static class ScrollBar {

    private int increment;
    private int pageIncrement;
    private int selection;
    private int thumb;
    private int maximum;
    private int minimum;
    private Composite parent;
    private Display display;

    private int defaultWidth = 15;
    private int defaultHeight = 100;
    private int width;
    private int height;

    private Rectangle headArrow;
    private Rectangle tailArrow;
    private Rectangle shaftBody;
    private Rectangle thumbRect;
    private Rectangle scrollBox;

    private Boolean isHasArrow = true;
    private Set<Style> styles;
    private Composite body;

    private Color shaftBodyColor;
    private Color arrowColor;
    private Color thumbColor;
    private Color thumbFrameColor;
    private Color bodyColor;

    private Image shaftBodyImage;
    private Image arrowUpImage;
    private Image arrowDownImage;
    private Image thumbImage;

    private ImageData shaftBodyImageData;
    private ImageData arrowUpImageData;
    private ImageData arrowDownImageData;
    private ImageData thumbImageData;

    private RGB innerRGB;

    private int dragStartY = -1;
    private int dragEndY = -1;
    private boolean canDrawForDrag = false;
    private int dragStartX = -1;
    private int dragEndX = -1;

    private boolean isVertical = true;

    private boolean isVisiable = true;

    private List<SelectionListener> sListeners = new ArrayList<SelectionListener>();
    /**
     * change 0 no change; change 1 upDownChange; change 2 downUpChange;
     */
    private int change = 0;
    ExecutorService exectorService = Executors.newCachedThreadPool();

    public enum Style {
      normal, simple, beauty;
      public static final EnumSet<Style> WHOLE_STYLES = EnumSet.allOf(Style.class);

      public Style next() {
        switch (this) {
          case normal: {
            return simple;
          }
          case simple: {
            return beauty;
          }
          case beauty: {
            return normal;
          }
        }
        return null;
      }
    }

    public ScrollBar(Composite parent, int style) {
      init(parent, changeInt2Styles(style));
    }

    public ScrollBar(Composite parent, Set<Style> styles) {
      init(parent, styles);
    }

    public void setVertical() {
      this.isVertical = true;
    }

    public void sethorizontal() {
      this.isVertical = false;
    }

    public boolean getIsVertical() {
      return this.isVertical;
    }

    public void disposeResource() {
      exectorService.shutdown();
      if (shaftBodyImage != null) {
        shaftBodyImage.dispose();
      }
      if (arrowDownImage != null) {
        arrowDownImage.dispose();
      }
      if (arrowUpImage != null) {
        arrowUpImage.dispose();
      }
      if (thumbImage != null) {
        thumbImage.dispose();
      }
      if (shaftBodyColor != null) {
        shaftBodyColor.dispose();
      }
      if (arrowColor != null) {
        arrowColor.dispose();
      }
      if (thumbColor != null) {
        thumbColor.dispose();
      }
    }

    public void addPaintListener(PaintListener pl) {
      body.addPaintListener(pl);
    }

    public void init(Composite parent, Set<Style> styles) {
      this.parent = parent;
      this.styles = styles;
      this.display = parent.getDisplay();
      if (isVertical) {
        defaultHeight = 100;
        defaultWidth = 15;
      } else {
        defaultHeight = 15;
        defaultWidth = 100;
      }
      body = new Composite(parent, SWT.NONE);
      body.setSize(defaultWidth, defaultHeight);
      shaftBody = new Rectangle(0, 0, defaultWidth, defaultHeight);
      innerRGB = new RGB(0, 0, 0);
      createWidget();
      initColors();
      initImages();
      body.addPaintListener(new PaintListener() {
        @Override
        public void paintControl(PaintEvent e) {
          draw();
        }
      });
      body.addListener(SWT.MouseDown, new Listener() {

        @Override
        public void handleEvent(Event event) {
        }
      });
      body.addDisposeListener(new DisposeListener() {

        @Override
        public void widgetDisposed(DisposeEvent e) {
          disposeResource();
        }
      });
      body.addDragDetectListener(new DragDetectListener() {

        @Override
        public void dragDetected(DragDetectEvent e) {
        }
      });
      body.addMouseMoveListener(new MouseMoveListener() {

        @Override
        public void mouseMove(MouseEvent e) {
          if (isVertical) {
            if (canDrawForDrag) {
              dragEndY = e.y;
              setThumbRect(thumbRect.x, thumbRect.y + dragEndY - dragStartY, thumbRect.width, thumbRect.height);
              draw();
              dragStartY = dragEndY;
              notifyListener(e);
              System.out.println(getSelection());
            } else {
              dragStartY = e.y;
            }
          } else {
            if (canDrawForDrag) {
              dragEndX = e.x;
              setThumbRect(thumbRect.x + dragEndX - dragStartX, thumbRect.y, thumbRect.width, thumbRect.height);
              draw();
              dragStartX = dragEndX;
              notifyListener(e);
              System.out.println(getSelection());
            } else {
              dragStartX = e.x;
            }
          }
        }
      });
      body.addMouseListener(new MouseListener() {
        volatile boolean flag = true;
        volatile boolean hasOne = false;
        AtomicInteger atomic = new AtomicInteger(0);
        volatile boolean leavingMD = false;

        @Override
        public void mouseUp(MouseEvent e) {
          flag = true;
          canDrawForDrag = false;
        }

        @Override
        public void mouseDown(final MouseEvent e) {

          int eventType = getEventType(e);
          // for no delay I put the drag event out of mouseDownExec;
          if (eventType == 4) {
            canDrawForDrag = true;
            if (isVertical) {
              dragStartY = e.y;
            } else {
              dragStartX = e.x;
            }
            return;
          }
          leavingMD = false;
          e.data = atomic.incrementAndGet();
          if (hasOne) {
            mouseDownExec(e);
            notifyListener(e);
            return;
          } else {
            hasOne = true;
            if (flag) {
              flag = false;
            }
            exectorService.execute(new Runnable() {
              @Override
              public void run() {
                hasOne = true;
                if (!leavingMD) {
                  try {
                    Thread.sleep(10);
                  } catch (InterruptedException e1) {
                    e1.printStackTrace();
                  }
                }
                mouseDownExec(e);
                notifyListener(e);
                if (!flag) {

                }
                try {
                  Thread.sleep(100);
                } catch (InterruptedException e1) {
                  e1.printStackTrace();
                }
                while (!flag) {
                  mouseDownExec(e);
                  notifyListener(e);
                  try {
                    Thread.sleep(100);
                  } catch (InterruptedException e1) {
                    e1.printStackTrace();
                  }
                }
                hasOne = false;
              }
            });
            leavingMD = true;
          }
        }

        @Override
        public void mouseDoubleClick(MouseEvent e) {
        }
      });
    }

    public void notifyListener(final MouseEvent e) {
      display.syncExec(new Runnable() {
        public void run() {
          for (SelectionListener sl : sListeners) {
            sl.widgetSelected(createSelectionEvent(e));
          }
        }
      });
    }

    public SelectionEvent createSelectionEvent(MouseEvent e) {
      Event ee = new Event();
      ee.widget = body;
      ee.display = display;
      ee.doit = true;
      ee.x = e.x;
      ee.y = e.y;
      SelectionEvent event = new SelectionEvent(ee);
      int eventType = getEventType(e);
      if (isHasArrow) {
        if (isVertical) {
          if (eventType == 0) {
            event.detail = SWT.ARROW_DOWN;
          }
          if (eventType == 1) {
            event.detail = SWT.ARROW_UP;
          }
        } else {
          if (eventType == 0) {
            event.detail = SWT.ARROW_LEFT;
          }
          if (eventType == 1) {
            event.detail = SWT.ARROW_RIGHT;
          }
        }
      } else {
        if (eventType == 0 || eventType == 1) {
          event.detail = SWT.ERROR;
        }
      }
      if (eventType == -1) {
        event.detail = SWT.ERROR;
      }
      if (eventType == 2) {
        event.detail = SWT.PAGE_DOWN;
      }
      if (eventType == 3) {
        event.detail = SWT.PAGE_UP;
      }
      return event;
    }

    public void initImages() {
      shaftBodyImage = null;
      arrowDownImage = null;
      arrowUpImage = null;
      thumbImage = null;

      shaftBodyImageData = null;
      arrowDownImageData = null;
      arrowUpImageData = null;
      thumbImageData = null;
    }

    public void mouseDownExec(final MouseEvent e) {
      display.syncExec(new Runnable() {

        @Override
        public void run() {
          int eX = e.x;
          int eY = e.y;
          int eventType = getEventType(eX, eY);

          if (eventType == -1) {
            return;
          }
          if (eventType == 0) {
            setSelection(getSelection() + increment);
          }
          if (eventType == 1) {
            setSelection(getSelection() - increment);
          }
          if (eventType == 2) {
            setSelection(getSelection() + pageIncrement);
          }
          if (eventType == 3) {
            setSelection(getSelection() - pageIncrement);
          }
          draw();
        }
      });
    }

    public int getEventType(Event e) {
      return getEventType(e.x, e.y);
    }

    public int getEventType(MouseEvent e) {
      return getEventType(e.x, e.y);
    }

    /*
     * eventType value |error -1 |arrowDown 0 |arrowUp 1 |pageDown 2 |pageUp 3
     * |drag 4|
     */
    public int getEventType(int ex, int ey) {
      if (isVertical) {
        if (isHasArrow) {
          if (ey < headArrow.y + headArrow.height) {
            return 1;
          } else {
            if (ey < thumbRect.y) {
              return 3;
            } else {
              if (ey < thumbRect.y + thumbRect.height) {
                return 4;
              } else {
                if (ey < tailArrow.y) {
                  return 2;
                } else {
                  if (ey < tailArrow.y + tailArrow.height) {
                    return 0;
                  } else {
                    return -1;
                  }
                }
              }
            }
          }
        } else {
          if (ey < thumbRect.y) {
            return 3;
          } else {
            if (ey < thumbRect.y + thumbRect.height) {
              return 4;
            } else {
              if (ey < shaftBody.height) {
                return 2;
              } else {
                return -1;
              }
            }
          }
        }
      } else {
        if (isHasArrow) {
          if (ex < headArrow.x + headArrow.width) {
            return 1;
          } else {
            if (ex < thumbRect.x) {
              return 3;
            } else {
              if (ex < thumbRect.x + thumbRect.width) {
                return 4;
              } else {
                if (ex < tailArrow.x) {
                  return 2;
                } else {
                  if (ex < tailArrow.x + tailArrow.width) {
                    return 0;
                  } else {
                    return -1;
                  }
                }
              }
            }
          }
        } else {
          if (ex < thumbRect.x) {
            return 3;
          } else {
            if (ex < thumbRect.x + thumbRect.width) {
              return 4;
            } else {
              if (ex < shaftBody.width) {
                return 2;
              } else {
                return -1;
              }
            }
          }
        }
      }
    }

    public int pixelChange2Scale(int y) {
      double oneScale = (double) shaftBody.height / ((double) (maximum - minimum));
      double tmpScale = (double) ((double) y / oneScale);
      return (int) tmpScale;
    }

    public void addSelectionListener(SelectionListener listener) {
      if (listener == null) {
        return;
      }
      sListeners.add(listener);
    }

    public RGB setInnerRGB(int r, int b, int g) {
      innerRGB.red = r;
      innerRGB.blue = b;
      innerRGB.green = g;
      return innerRGB;
    }

    public void initColors() {
      initBodyColor();

      shaftBodyColor = new Color(display, setInnerRGB(240, 240, 240));
      arrowColor = new Color(display, setInnerRGB(150, 150, 150));
      thumbColor = new Color(display, setInnerRGB(200, 200, 200));
      thumbFrameColor = new Color(display, setInnerRGB(150, 150, 150));
    }

    public void initBodyColor() {
      bodyColor = display.getSystemColor(SWT.COLOR_WHITE);
    }

    private void createWidget() {
      increment = 1;
      pageIncrement = 10;
      selection = 0;
      thumb = 10;
      maximum = 100;
      minimum = 0;

      updateBody(defaultWidth, defaultHeight);
    }

    public void updateBody(int _width, int _height) {
      width = _width;
      height = _height;
      if (width < shaftBody.width) {
        setShaftBodyWidth(width);
      }
      if (height < shaftBody.height) {
        setShaftBodyHeight(height);
      }
      if (styles.contains(Style.normal)) {
        // headArrow and tailArrow are square;
        if (isVertical) {
          headArrow = new Rectangle(0, 0, width, width);
          tailArrow = new Rectangle(0, height - width, width, width);
        } else {
          headArrow = new Rectangle(0, 0, height, height);
          tailArrow = new Rectangle(width - height, 0, height, height);
        }
        isHasArrow = true;
      }
      if (styles.contains(Style.simple)) {
        headArrow = null;
        tailArrow = null;
        isHasArrow = false;
      }
      if (styles.contains(Style.beauty)) {
        headArrow = null;
        tailArrow = null;
        isHasArrow = false;
      }
      if (change == 1 || change == 0) {
        thumbRect = computeThumbRect();
        System.out.println("change 1 thumbRect:" + thumbRect);
      }
      if (change == 2) {
        int tmpSelection = computeSelection();
        setSelection(tmpSelection);
        System.out.println("change 2 thumbRect:" + thumbRect);
      }
    }

    public Boolean getIsHasArrow() {
      return isHasArrow;
    }

    public int computeSelection() {
      double tmp;
      if (isHasArrow) {
        if (isVertical) {
          tmp = (double) ((double) thumbRect.y - (double) headArrow.height)
              / (double) ((double) shaftBody.height - (double) headArrow.height * 2);
        } else {
          tmp = (double) ((double) thumbRect.x - (double) headArrow.width)
              / (double) ((double) shaftBody.width - (double) headArrow.width * 2);
        }
      } else {
        if (isVertical) {
          tmp = (double) thumbRect.y / (double) shaftBody.height;
        } else {
          tmp = (double) thumbRect.x / (double) shaftBody.width;
        }
      }
      return (int) (tmp * ((double) (maximum - minimum)));
    }

    public Rectangle computeScrollBox() {
      if (headArrow == null && tailArrow == null) {
        return new Rectangle(0, 0, width, height);
      } else {
        if (isVertical) {
          return new Rectangle(0, headArrow.height, width, height - headArrow.height * 2);
        } else {
          return new Rectangle(headArrow.width, 0, width - headArrow.width * 2, height);
        }
      }
    }

    public Rectangle computeThumbRect() {
      scrollBox = computeScrollBox();
      double scaleHeight, thumbHeight, thumbWidth, thumbX, thumbY;
      if (isVertical) {
        scaleHeight = scrollBox.height / ((double) maximum - (double) minimum);
        thumbHeight = ((double) thumb) * scaleHeight;
        thumbWidth = scrollBox.width;
        thumbX = scrollBox.x;
        thumbY = Math.ceil(scaleHeight * selection + (double) scrollBox.y);
        System.out.println("compute:thumbY:" + thumbY);
      } else {
        scaleHeight = (double) scrollBox.width / ((double) maximum - (double) minimum);
        thumbWidth = ((double) thumb) * scaleHeight;
        thumbHeight = scrollBox.height;
        thumbX = Math.ceil(scaleHeight * selection + (double) scrollBox.x);
        thumbY = scrollBox.y;
        System.out.println("compute:thumbX:" + thumbX);
      }
      return new Rectangle((int) thumbX, (int) thumbY, (int) thumbWidth, (int) thumbHeight);
    }

    public void tmpPrint(int[] array) {
      for (int i = 0; i < array.length; i++) {
        System.out.print(array[i] + " ");
      }
      System.out.println();
    }

    public Rectangle getThumbRect() {
      return thumbRect;
    }

    public void setShaftBodyHeight(int _height) {
      if (_height > height) {
        return;
      } else {
        shaftBody.height = _height;
      }
    }

    public void setShaftBodyWidth(int _width) {
      if (_width > width - shaftBody.x) {
        return;
      } else {
        shaftBody.width = _width;
        if (isVertical) {
          shaftBody.x = (this.width - _width) / 2;
        }
      }
    }

    public void setShaftBodyRect(int x, int y, int _width, int _height) {
      if (y + _height > this.height) {
        return;
      }
      if (x + _width > this.width) {
        return;
      }
      shaftBody.x = x;
      shaftBody.y = y;
      shaftBody.width = _width;
      shaftBody.height = _height;
    }

    public void setShaftBodyRect(Rectangle rect) {
      setShaftBodyRect(rect.x, rect.y, rect.width, rect.height);
    }

    public void setThumbRect(int x, int y, int _width, int _height) {
      change = 2;
      int tmpMin, tmpMax;
      if (isVertical) {
        if (isHasArrow) {
          tmpMin = headArrow.y + headArrow.height;
          tmpMax = shaftBody.height - tailArrow.height;
        } else {
          tmpMin = 0;
          tmpMax = shaftBody.height;
        }
        System.out.println("setRect:Max:" + tmpMax + "setRect:min:" + tmpMin);
        if (y + _height > tmpMax) {
          y = tmpMax - _height;
          return;
        } else {
          if (y < tmpMin) {
            y = tmpMin;
          } else {
            thumbRect.x = x;
            thumbRect.y = y;
            thumbRect.width = _width;
            thumbRect.height = _height;
          }
        }
      } else {
        if (isHasArrow) {
          tmpMin = headArrow.x + headArrow.width;
          tmpMax = shaftBody.width - tailArrow.width;
        } else {
          tmpMin = 0;
          tmpMax = shaftBody.width;
        }
        System.out.println("setRect:Max:" + tmpMax + "setRect:min:" + tmpMin);
        if (x + _width > tmpMax) {
          x = tmpMax - _width;
          return;
        } else {
          if (x < tmpMin) {
            x = tmpMin;
          } else {
            thumbRect.x = x;
            thumbRect.y = y;
            thumbRect.width = _width;
            thumbRect.height = _height;
          }
        }
      }
    }

    public void setThumbRect(Rectangle rect) {
      setThumbRect(rect.x, rect.y, rect.width, rect.height);
    }

    public void drawShaftBody(GC gc) {
      if (shaftBodyImage == null) {
        gc.setBackground(shaftBodyColor);
        gc.fillRectangle(shaftBody);
        System.out.println("shaftBody:" + shaftBody);
      } else {
        gc.drawImage(shaftBodyImage, 0, 0, shaftBodyImageData.width, shaftBodyImageData.height, shaftBody.x,
            shaftBody.y, shaftBody.width, shaftBody.height);
      }
    }

    public void drawArrow(GC gc) {
      if (isVertical) {
        gc.setBackground(arrowColor);
        int[] points = getUpArrow(headArrow);
        System.out.println("headArrow:" + headArrow);
        gc.fillPolygon(points);
        points = getDownArrow(tailArrow);
        System.out.println("tailArrow:" + tailArrow);
        gc.fillPolygon(points);
      } else {
        gc.setBackground(arrowColor);
        int[] points = getLeftArrow(headArrow);
        gc.fillPolygon(points);
        points = getRightArrow(tailArrow);
        gc.fillPolygon(points);
      }
    }

    public void drawThumb(GC gc) {
      if (thumbImage == null) {
        gc.setForeground(thumbFrameColor);
        System.out.println("thumbRect:"+thumbRect);
        if (isVertical) {
          gc.drawRoundRectangle(thumbRect.x, thumbRect.y, thumbRect.width - 1, thumbRect.height - 1, 3, 3);
          gc.setBackground(thumbColor);
          gc.fillRoundRectangle(thumbRect.x + 1, thumbRect.y + 1, thumbRect.width - 2, thumbRect.height - 2, 1, 1);
        } else {
          gc.drawRoundRectangle(thumbRect.x, thumbRect.y, thumbRect.width - 1, thumbRect.height - 1, 3, 3);
          gc.setBackground(thumbColor);
          gc.fillRoundRectangle(thumbRect.x + 1, thumbRect.y + 1, thumbRect.width - 2, thumbRect.height - 2, 1, 1);
        }
      } else {
        gc.drawImage(thumbImage, 0, 0, thumbImageData.width, thumbImageData.height, thumbRect.x, thumbRect.y,
            thumbRect.width, thumbRect.height);
      }
    }

    public void drawBody(GC gc) {
      gc.setBackground(bodyColor);
      gc.setForeground(bodyColor);
      gc.fillRectangle(0, 0, width, height);
    }

    public void draw() {
      if (isVisiable) {
        GC gc = new GC(body);
        updateBody(width, height);
        drawBody(gc);
        if (shaftBody != null) {
          drawShaftBody(gc);
        }
        if (headArrow != null && tailArrow != null) {
          drawArrow(gc);
        }
        if (thumbRect != null) {
          drawThumb(gc);
        }
        change = 0;
        gc.dispose();
      }
    }

    public int[] getUpArrow(Rectangle rect) {
      return new int[] { rect.x + rect.width / 2, rect.y + rect.height / 2, rect.x + rect.width / 4,
          rect.y + rect.height * 3 / 4, rect.x + rect.width * 3 / 4, rect.y + rect.height * 3 / 4 };
    }

    public int[] getDownArrow(Rectangle rect) {
      return new int[] { rect.x + rect.width / 4, rect.y + rect.height / 4, rect.x + rect.width * 3 / 4,
          rect.y + rect.height / 4, rect.x + rect.width / 2, rect.y + rect.height / 2 };
    }

    public int[] getLeftArrow(Rectangle rect) {
      return new int[] { rect.x + rect.width / 2, rect.y + rect.height / 2, rect.x + rect.width * 3 / 4,
          rect.y + rect.height / 4, rect.x + rect.width * 3 / 4, rect.y + rect.height * 3 / 4 };
    }

    public int[] getRightArrow(Rectangle rect) {
      return new int[] { rect.x + rect.width / 4, rect.y + rect.height / 4, rect.x + rect.width / 4,
          rect.y + rect.height * 3 / 4, rect.x + rect.width / 2, rect.y + rect.height / 2 };
    }

    public Set<Style> changeInt2Styles(int style) {
      EnumSet<Style> tmp = EnumSet.noneOf(Style.class);
      int j = 1;
      Style tmpStyle = Style.normal;
      for (int i = 0; i < 3; i++) {
        if ((style & j) != 0) {
          tmp.add(tmpStyle);
        }
        j = j << 1;
        tmpStyle = tmpStyle.next();
      }
      return tmp;
    }

    public void setBounds(int x, int y, int width, int height) {
      body.setBounds(x, y, width, height);
      updateBody(width, height);
      setShaftBodyWidth(width);
      setShaftBodyHeight(height);
    }

    public void setBounds(Rectangle rect) {
      setBounds(rect.x, rect.y, rect.width, rect.height);
    }

    public void setWidth(int value) {
      if (value < 1) {
        return;
      }
      width = value;
    }

    public void setHeight(int value) {
      if (value < 1) {
        return;
      }
      height = value;
    }

    public void setIncrement(int value) {
      if (value < 1) {
        return;
      }
      increment = value;
    }

    public void setPageIncrement(int value) {
      if (value < 1) {
        return;
      }
      pageIncrement = value;
    }

    public void setSelection(int value) {
      // changable

      if (minimum > value) {
        selection = minimum;
        return;
      }
      if (value > ((maximum - minimum) - pageIncrement)) {
        selection = ((maximum - minimum) - pageIncrement);
        return;
      }
      selection = value;
    }

    public boolean getIsVisible(){
      return isVisiable;
    }
    
    public void setVisible(boolean visible) {
      isVisiable = visible;
    }

    public void setThumb(int value) {
      if (value < 1) {
        return;
      }
      thumb = value;
    }

    public void setMaximum(int value) {
      if (value < 0) {
        return;
      } else {
        maximum = value;
      }
    }

    public void setMinimum(int value) {
      if (value < 0) {
        return;
      } else {
        minimum = value;
      }
    }

    public void setStyle(Set<Style> styles) {

    }

    public void setBackageColor(int r, int g, int b) {
      setInnerRGB(r, b, g);
      setSharfBodyColor(new Color(display, innerRGB));
    }

    public void setArowColor(int r, int g, int b) {
      setInnerRGB(r, b, g);
      setArrowColor(new Color(display, innerRGB));
    }

    public void setForegroundColor(int r, int g, int b) {
      setInnerRGB(r, b, g);
      setThumbColor(new Color(display, innerRGB));
    }

    public void setSharfBodyColor(Color color) {
      shaftBodyColor = color;
    }

    public void setArrowColor(Color color) {
      if (arrowColor != null) {
        arrowColor.dispose();
      }
      arrowColor = color;
    }

    public void setThumbColor(Color color) {
      if (thumbColor != null) {
        thumbColor.dispose();
      }
      thumbColor = color;
    }

    public void setThumbFrameColor(Color color) {
      if (thumbFrameColor != null) {
        thumbFrameColor.dispose();
      }
      thumbFrameColor = color;
    }

    public void setArrowUpImage(String url) {
      arrowUpImageData = new ImageData(url);
      arrowUpImage = new Image(display, arrowUpImageData);
    }

    public void setArrowDownImage(String url) {
      arrowDownImageData = new ImageData(url);
      arrowDownImage = new Image(display, arrowDownImageData);
    }

    public void setBackageImage(String url) {
      shaftBodyImageData = new ImageData(url);
      shaftBodyImage = new Image(display, shaftBodyImageData);
    }

    public void setForegroundImage(String url) {
      thumbImageData = new ImageData(url);
      thumbImage = new Image(display, thumbImageData);
    }

    public Rectangle getBounds(){
      return body.getBounds();
    }
    
    public Shell getShell() {
      return parent.getShell();
    }

    public Composite getParent() {
      return parent;
    }

    public int getIncrement() {
      return increment;
    }

    public int getPageIncrement() {
      return pageIncrement;
    }

    public int getMaximum() {
      return maximum;
    }

    public int getMinimum() {
      return minimum;
    }

    public int getSelection() {
      return selection;
    }

    public int getThumb() {
      return thumb;
    }
  }
}