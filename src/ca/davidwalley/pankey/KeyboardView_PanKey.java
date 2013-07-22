// KeyboardView_PanKey.java - Bulk of programming for Pan Key.                                  //                             [2013-06-28 davidwalley.ca F166]
// (c) 2013 David C. Walley
// This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/.

                                                                                                //
package ca.davidwalley.pankey;                                                                  //                             [2013-06-28 davidwalley.ca 6F19]
                                                                                                //
import android.util.*;                                                                          // For ease of debug logging.    [2013-06-28 davidwalley.ca 4FCD]
                                                                                                //
public class /////////////////////////////////////////////////////////////////////////////////////                             [2013-06-28 davidwalley.ca 5389]
                                   KeyboardView_PanKey ///////////////////////////////////////////                             [2013-06-28 davidwalley.ca C886]
extends android.inputmethodservice.KeyboardView //////////////////////////////////////////////////                             [2013-06-28 davidwalley.ca 8777]
//extends                     android.view.View //////////////////////////////////////////////////                             [2013-06-28 davidwalley.ca AED3]
{/////////////////////////////////////////////////////////////////////////////////////////////////                             [2013-06-28 davidwalley.ca 80BA]
                                                                                                //
 private static final String    sTAG                    = "PanKey";                             // For debug logging.

 android.content.Context        p_context               = null;                                 //
                                                                                                //
 public                         ////////////////////////////////////////////////////////////////// Constructor
                                KeyboardView_PanKey(//                                          //
  android.content.Context        a_context                                                      //
 ,android.util.AttributeSet      a                                                              //
 ){                             //////////////////////////////////////////////////////////////////
  super(a_context, a   );                                                                       //
  Log.d(sTAG,"KeyboardView_PanKey.KeyboardView_PanKey 1");
  p_context = a_context;                                                                        // Call the parent class's constructor.    [2013-06-28 davidwalley.ca 08F6]
  p_SetupKeyboard();                                                                            // Initialize the keyboard layout arrays.
 }////////////////////////////////////////////////////////////////////////////////////////////////
                                                                                                //
                                                                                                //
 public                         ////////////////////////////////////////////////////////////////// Constructor
                                KeyboardView_PanKey(//                                          //
  android.content.Context        a_context                                                      //
 ,android.util.AttributeSet      a                                                              //
 ,int                            d                                                              //
 ){                             //////////////////////////////////////////////////////////////////
  super(a_context, a, d);                                                                       // Call the parent class's constructor.    [2013-06-28 davidwalley.ca 30DB]
  Log.d(sTAG,"KeyboardView_PanKey.KeyboardView_PanKey 2");
  p_context = a_context;                                                                        //
  p_SetupKeyboard();                                                                            // Initialize the keyboard layout arrays.
 }////////////////////////////////////////////////////////////////////////////////////////////////
                                                                                                //
                                                                                                //
 private String                 sDEBUG0                 = "X";                                  //
 private String                 sDEBUG1                 = "";                                   //
 private String                 sDEBUG2                 = "";                                   //
 private String                 sDEBUG3                 = "";                                   //
 private String                 sDEBUG4                 = "";                                   //
                                                                                                //
 private final int              p_colorHIGH             = 0xffFFFF00;                           //
 private final int              p_colorFORE             = 0xffFFFFFF;                           //
 private final int              p_colorDIM              = 0xff888888;                           //
                                                                                                //
 private android.graphics.Paint   p_paint               = new android.graphics.Paint();         // General purpose paint object for drawing on screen.                            [2013-06-28 davidwalley.ca 710B]
                                                                                                //
 private int                    p_nKeyAreaL_px          = 0;                                    // Position on screen (left and
 private int                    p_nKeyAreaT_px          = 0;                                    // top) of keyboard display area.
 private float                  p_dKeyAreaW_px          = 100.f;                                // Width and
 private float                  p_dKeyAreaH_px          = 100.f;                                // height of keyboard display area, in pixels, as set in p_NewKeyAreaSize().
 private float                  p_dScreenW_px           = 0;                                    // Total screen width and            [2013-06-28 davidwalley.ca 318A]
 private float                  p_dScreenH_px           = 0;                                    // height in pixels.                     [2013-06-28 davidwalley.ca A264]

 private boolean                p_bRightHanded          = false;                                // Remember if the user started as if right-handed or left-handed.

 private float                  p_dTouchPanX_px         = -99.f;                                // Current screen co-    [2013-06-28 davidwalley.ca 0C26]
 private float                  p_dTouchPanY_px         = -99.f;                                // ordinates of the pan thumb (keyboard slider), as a fraction of the screen size.    [2013-06-28 davidwalley.ca 906E]
 private boolean                p_bPanTouching          = false;                                // Above values are only valid if this is set to true.
 private int                    p_isPanAt               = 2;

 private float                  p_dTouchKeyX_1          = -99.f;                                // Remember the current co-    [2013-06-28 davidwalley.ca 864E]
 private float                  p_dTouchKeyY_1          = -99.f;                                // ordinates of the right finger (key selector), as a fraction of the screen size.    [2013-06-28 davidwalley.ca 6A5E]
 private boolean                p_bKeyTouching          = false;                                // Above values are only valid if this is set to true.

 private int                    p_idTouchPan            = 0;                                    // Touch-point ID assigned to the pan thumb.                            [2013-06-28 davidwalley.ca 4E11]
 private int                    p_idTouchKey            = 0;                                    // Touch-point ID assigned to the pan thumb.
                                                                                                //
 private int[][]                p_anAscii               = new int[9][12];                       // ASCII codes (for display) of keys on keyboard.
 private int[][]                p_anShift               = new int[9][12];                       // Shift and
 private int[][]                p_anCode                = new int[9][12];                       // keycode values for sending keystrokes to client editor application.

 private android.graphics.Bitmap  p_bitmapFinger_L      = null;                                 // Remember bitmaps for the background,   [2012-04-05 dave@davewalley.ca BF42 2012-06-15 davidwalley.ca]
 private android.graphics.Bitmap  p_bitmapFinger_R      = null;                                 // Remember bitmaps for the background,   [2012-04-05 dave@davewalley.ca BF42 2012-06-15 davidwalley.ca]

 View_Popup                     p_view_popup            = null;                                 // Create a view for a popup that will overlay the display and show guidelines.
 android.widget.PopupWindow     p_popupwindow           = null;                                 // Create a new popup window which can display the contentView. The dimension of the window must be passed to this constructor.

 private final int              p_isINTRO               = 0;                                    // State before anything has been pressed.
 private final int              p_isPANdOWN             = 1;                                    // First thumb pressed down and held.
 private final int              p_isCONFIRM             = 2;                                    // Finger up to select key.
 private int                    p_isState               = p_isINTRO;                            // Remember the state, initially "intro".


 public void                    //////////////////////////////////////////////////////////////////
                                CleanUp(//                                                      // Clean up on shut down - in particular, dismiss any popup window.
 ){                             //////////////////////////////////////////////////////////////////
  Log.d(sTAG,"KeyboardView_PanKey.CleanUp 1");
  if( null != p_popupwindow ){
   p_popupwindow.dismiss(); p_popupwindow = null; p_view_popup = null;
  }//if
  p_isState = p_isINTRO;
  p_bPanTouching = p_bKeyTouching = false;
 }//CleanUp///////////////////////////////////////////////////////////////////////////////////


 @Override public void          ////////////////////////////////////////////////////////////// Called during layout when
                                onSizeChanged(//                                            // the size of this view has changed. If you were just added to the view hierarchy, you're called with the old values of 0.
  int                            w                                                          // Current width of this view.
 ,int                            h                                                          // Current height of this view.
 ,int                            oldw                                                       // Old width of this view.
 ,int                            oldh                                                       // Old height of this view.
 ){                             /////////////////////////////////////////////////////////////
  Log.d(sTAG,"KeyboardView_PanKey.onSizeChanged 1");
  if( null != p_popupwindow ){
   p_popupwindow.dismiss(); p_popupwindow = null; p_view_popup = null;
  }//if
 }///onSizeChanged///////////////////////////////////////////////////////////////////////////


 @Override public void          /////////////////////////////////////////////////////////////////                             [2013-06-28 davidwalley.ca 27B9]
                                onDraw(//                                                       // Implement this to do your drawing.    [2013-06-28 davidwalley.ca 270C]
  final android.graphics.Canvas  a_canvas                                                       // the canvas on which the background will be drawn.    [2013-06-28 davidwalley.ca 7C82]
 ){                             //////////////////////////////////////////////////////////////////                             [2013-06-28 davidwalley.ca 93A6]
  Log.d(sTAG,"KeyboardView_PanKey.onDraw 1");
  ///        ---                +-------------------------------------------------------+       //                             [2013-06-28 davidwalley.ca 6E6B]
  ///         |                 _\              Main display - after initial            _\      //                             [2013-06-28 davidwalley.ca C63C]
  ///         |                 \               touch, fingers can move into            \       //                             [2013-06-28 davidwalley.ca D423]
  ///         |                 |               this area.                              |       //                             [2013-06-28 davidwalley.ca 231A]
  ///         |      --- 0.0    +----------------------------+--------------------------+       //                             [2013-06-28 davidwalley.ca D7B6]
  ///         |       ^         |                                                       |       //                             [2013-06-28 davidwalley.ca 094D]
  /// p_dScreenH_px   |         |   "Pan"                Keyboard              "Key"    |       //                             [2013-06-28 davidwalley.ca 7816]
  ///         | p_dKeyAreaH_px  |  finger                  Area               finger    |       //                             [2013-06-28 davidwalley.ca C2BA]
  ///         |       |         |  (moves                                    (chooses   |       //                             [2013-06-28 davidwalley.ca DE1B]
  ///         v       v         | keyboard)                                    key)     |       //                             [2013-06-28 davidwalley.ca F306]
  ///        ---     --- 1.0    +----------------------------+--------------------------+       //                             [2013-06-28 davidwalley.ca BA27]
  ///                          0.0                          0.5                        1.0      //                             [2013-06-28 davidwalley.ca 9544]
  ///                           |<------------------- p_dKeyAreaW_px ------------------>|       //                             [2013-06-28 davidwalley.ca 48D1]
  ///                           |<------------------- p_dScreenW_px ------------------->|       //                             [2013-06-28 davidwalley.ca C627]
                                                                                                //
  a_canvas.drawColor(0xff883322);
  p_paint.setStyle(    android.graphics.Paint.Style.FILL  );
  p_paint.setTextAlign(android.graphics.Paint.Align.CENTER);
  p_paint.setColor(p_colorFORE); p_paint.setTextSize(18);

  p_NewKeyAreaSize(); p_FindDisplaySize();                                                    // Make sure the key area and entire device display size info is up-to-date, and update resources if there has been a change.

  p_paint.setTextAlign(android.graphics.Paint.Align.LEFT); p_paint.setColor(0xffFF00FF);
  a_canvas.drawText(sDEBUG0 , 10.f , 20.f ,p_paint);
  a_canvas.drawText(sDEBUG1 , 10.f , 40.f ,p_paint);
  a_canvas.drawText(sDEBUG2 , 10.f , 60.f ,p_paint);
  a_canvas.drawText(sDEBUG3 ,150.f , 80.f ,p_paint);
  a_canvas.drawText(sDEBUG4 ,150.f , 40.f ,p_paint);

  p_paint.setTextAlign(android.graphics.Paint.Align.CENTER); p_paint.setColor(0xff000000);
  if( p_isINTRO == p_isState ){
   if( null != p_popupwindow ){
    p_popupwindow.dismiss(); p_view_popup  = null; p_popupwindow = null;
   }//if
   a_canvas.drawBitmap(p_bitmapFinger_L ,                          0.f ,0.f ,null);
   a_canvas.drawBitmap(p_bitmapFinger_R ,p_dKeyAreaW_px-p_dKeyAreaH_px ,0.f ,null);
   a_canvas.drawText( "Press & hold a print" ,p_dKeyAreaW_px *0.5f ,p_dKeyAreaH_px *0.4f ,p_paint ); //                             [2013-06-28 davidwalley.ca C21A]
   a_canvas.drawText( "wih thumb. Slide up." ,p_dKeyAreaW_px *0.5f ,p_dKeyAreaH_px *0.7f ,p_paint ); //                             [2013-06-28 davidwalley.ca C21A]
 return;                                                                                        // We are done
  }//if                                                                                         // .

  if( p_isCONFIRM == p_isState ){
   if( null != p_popupwindow ){
    p_popupwindow.dismiss(); p_view_popup  = null; p_popupwindow = null;
   }//if
   a_canvas.drawBitmap(p_bitmapFinger_L ,                          0.f ,0.f ,null);
   a_canvas.drawBitmap(p_bitmapFinger_R ,p_dKeyAreaW_px-p_dKeyAreaH_px ,0.f ,null);
   a_canvas.drawText( "CONFIRM" ,p_dKeyAreaW_px *0.5f ,p_dKeyAreaH_px *0.4f ,p_paint );         //                             [2013-06-28 davidwalley.ca C21A]
   a_canvas.drawText( "UNDO"    ,p_dKeyAreaW_px *0.5f ,p_dKeyAreaH_px *0.7f ,p_paint );         //                             [2013-06-28 davidwalley.ca C21A]
 return;                                                                                        // We are done
  }//if                                                                                         // .

  if( null == p_popupwindow ){
   p_view_popup  = new View_Popup(this, p_context);
   p_popupwindow = new android.widget.PopupWindow(                                          // Create a new popup window which can display the contentView. The dimension of the window must be passed to this constructor.
    p_view_popup                                                                             // the popup's content.
   ,(int)p_dScreenW_px - 6                                                                // Width and
   ,(int)p_dScreenH_px - 6                                                                // height.
   ,false                                                                                 // can be focused.
   );
   p_popupwindow.showAtLocation(
    this                                                                                  // a parent view to get the getWindowToken() token from.
   ,android.view.Gravity.CENTER ,0 ,0                                                     // gravity the gravity which controls the placement of the popup window, the popup's x location offset
   );
  }else{
   p_view_popup.invalidate();
  }//if

  int                           iKeyColumn              = (int)Math.floor(4.f * p_dTouchKeyX_1);
  int                           iKeyRow                 = (int)Math.floor(6.f * p_dTouchKeyY_1);

  String                        s                       = "";                                   // Short-term utility for getting a string from an ASCII number.
  int                           nAscii                  = 0;                                    // Short-term utility.
  boolean                       bInZone                 = false;
  for( int iColumn = 0; iColumn < 12; iColumn++ ){
   bInZone = ( p_isPanAt - 1 == iColumn/4 );
   for(  int iRow    = 0; iRow    <  3; iRow++    ){
    if( !bInZone ){
     p_paint.setTextSize( 18); p_paint.setColor(p_colorDIM);
    }else{
     if( iKeyColumn == (iColumn%4)   &&   iKeyRow == iRow ){
      p_paint.setTextSize(24); p_paint.setColor(p_colorHIGH);
     }else{
      p_paint.setTextSize(18); p_paint.setColor(p_colorFORE);
    }}//if//if ! bInZone

    nAscii = p_anAscii[iRow][iColumn];
    if(         0 <= nAscii ){ s = java.lang.Character.toString( (char)nAscii );
    }else if(  -1 == nAscii ){ s = "??" ;    }else if(  -2 == nAscii ){ s = "esc";
    }else if(  -3 == nAscii ){ s = "del";    }else if(  -4 == nAscii ){ s = "ins";
    }else if(  -5 == nAscii ){ s = "tab";    }else if(  -6 == nAscii ){ s = "^^" ;
    }else if(  -7 == nAscii ){ s = "<-" ;    }else if(  -8 == nAscii ){ s = ">>" ;
    }else if(  -9 == nAscii ){ s = "vv" ;    }else{                     s = ".." ;
    }//if

    a_canvas.drawText(s
    ,p_dKeyAreaW_px * ( 1.5f + iColumn )/14.f
    ,p_dKeyAreaH_px * (  .8f + iRow    )/ 3.5f
    ,p_paint
    );
  }}//for iRow//for iColumn

  Log.d(sTAG,"KeyboardView_PanKey.onDraw 99");
 }///onDraw(){}///////////////////////////////////////////////////////////////////////////////////                             [2013-06-28 davidwalley.ca 8335]


 public void                    //////////////////////////////////////////////////////////////
                                DrawPopup(//                                                //
  final android.graphics.Canvas  a_canvas                                                   // the canvas on which the background will be drawn.    [2013-06-28 davidwalley.ca 7C82]
 ){                             //////////////////////////////////////////////////////////////                             [2013-06-28 davidwalley.ca 93A6]
  p_paint.setTextSize(50); p_paint.setColor(0xccFFFFFF);
  p_paint.setStyle(android.graphics.Paint.Style.STROKE);
  p_paint.setStrokeWidth(2);
  int                           nAscii                  = 0;
  String                        s                       = "";
  for( int iColumn = 0; iColumn < 4; iColumn++ ){
   for(  int iRow  = 0; iRow    < 3; iRow++    ){
    nAscii = p_anAscii[iRow][iColumn];
    if(         0 <= nAscii ){ s = java.lang.Character.toString( (char)nAscii );
    }else if(  -1 == nAscii ){ s = "??" ;    }else if(  -2 == nAscii ){ s = "esc";
    }else if(  -3 == nAscii ){ s = "del";    }else if(  -4 == nAscii ){ s = "ins";
    }else if(  -5 == nAscii ){ s = "tab";    }else if(  -6 == nAscii ){ s = "^^" ;
    }else if(  -7 == nAscii ){ s = "<-" ;    }else if(  -8 == nAscii ){ s = ">>" ;
    }else if(  -9 == nAscii ){ s = "vv" ;    }else{                     s = ".." ;
    }//if
    a_canvas.drawText(s
    ,p_dKeyAreaW_px * ( 0.5f + iColumn )/4.f
    ,p_dScreenH_px  * ( 1.5f + iRow    )/6.f
    ,p_paint
    );
  }}//for iRow//for iColumn
  p_paint.setStyle(android.graphics.Paint.Style.FILL);
 }//DrawPopup/////////////////////////////////////////////////////////////////////////////////


 @Override public boolean       //////////////////////////////////////////////////////////////////                             [2013-06-28 davidwalley.ca D88C]
                                onTouchEvent(//                                                 // Handle a press, change or movement on the touchscreen.    [2013-06-28 davidwalley.ca 6B36]
  android.view.MotionEvent       a_motionevent                                                  //                             [2013-06-28 davidwalley.ca C11F]
 ){                             //////////////////////////////////////////////////////////////////                             [2013-06-28 davidwalley.ca 93A6]
  Log.d(sTAG,"KeyboardView_PanKey.onTouchEvent 1");
  int                           isActionOnly        = a_motionevent.getActionMasked();          // Get the action we are responding to.
  if( isActionOnly == android.view.MotionEvent.ACTION_CANCEL ){                                 // If the current event is a cancellation of keboard operation, then    [2013-06-28 davidwalley.ca D532 2013-06-28 davidwalley.ca]
   p_bPanTouching = p_bKeyTouching = false;                                                     // Remember that both fingers are off-screen.    [2013-06-28 davidwalley.ca C446]
   p_isState = p_isINTRO;
   invalidate();                                                                                // Set up for a screen repaint        [2013-06-28 davidwalley.ca 38DD]

 return true;                                                                                   // and we are done                             [2013-06-28 davidwalley.ca 6823]
  }//if                                                                                         // .                            [2013-06-28 davidwalley.ca 5FB2]

  int                           iActionIndex        = a_motionevent.getActionIndex( );          // Get the index (within the list of touch-points) of the affected touch-point.    [2013-06-28 davidwalley.ca F2AB]
  int                           idActionsPointer    = a_motionevent.getPointerId(iActionIndex); // Get the ID of the affected touch-point (which is preserved between calls to this routine, whereas the index may not be).

  boolean                       bUpAction               =                                           // Remember if this event represents the disappearance of
      (   android.view.MotionEvent.ACTION_UP         == isActionOnly                            // the primary
       || android.view.MotionEvent.ACTION_POINTER_UP == isActionOnly                            // or some secondary touch-point
      );                                                                                        // .

  float                         dNewX_1                 = 0.f;                                  // Want to remember the co-
  float                         dNewY_1                 = 0.f;                                  // ordinates
  int                           idNew                   = 0;                                    // and ID of the first unrecognized touch-point.
  boolean                       bNewSeen                = false;
  boolean                       bPanSeen                = false;                                // Remember if we find the left finger in the list of touch-points.                            [2013-06-28 davidwalley.ca C94B]
  boolean                       bKeySeen                = false;                                // Remember if we find the right finger in the list of touch-points.                             [2013-06-28 davidwalley.ca EE8D]

  int                           id;                                                             // Short-term utility for remmbering touch-point IDs (as opposed to pointer indexes).

  p_FindDisplaySize();                                                                          // Make sure we have an up-to-date size of the entire display.

  /// DECODE LIST OF TOUCH-POINTS AND DETERMINE CURRENT STATE OF OPERATIONS //////////////////////
  for( int i = a_motionevent.getPointerCount(); 0 < i; ){ i--;                                  // Check all active 'pointers' (fingers touching the screen).
   id = a_motionevent.getPointerId(i);                                                          // Convert the list index to a touch-point ID.
   if( bUpAction   &&   idActionsPointer == id ){                                               // If this touch-point is the one that has been lifted, then
  continue;                                                                                     // move on to the next touch-point in the list
   }//if                                                                                        // .

   if(       p_bPanTouching   &&   id == p_idTouchPan ){                                        // If this is the (touching) pan finger, then
    bPanSeen = true;                                                                            // remember we have seen the right finger in the list.     [2013-06-28 davidwalley.ca BB2F]
    p_dTouchPanX_px = a_motionevent.getX(i);                                                 // Get the new co-                            [2013-06-28 davidwalley.ca 97B7]
    p_dTouchPanY_px = a_motionevent.getY(i);                                                   // ordinates of the left finger.                            [2013-06-28 davidwalley.ca B9B1]
   }else if( p_bKeyTouching   &&   id == p_idTouchKey ){                                        // If this is the (touching) right finger, then
    bKeySeen = true;                                                                            // remember we have seen the right finger in the list.     [2013-06-28 davidwalley.ca BB2F]
    p_dTouchKeyX_1 = (a_motionevent.getX(i) +p_nKeyAreaL_px) / p_dScreenW_px;                   // Calculate the new co-                                  [2013-06-28 davidwalley.ca 0BC3]
    p_dTouchKeyY_1 = (a_motionevent.getY(i) +p_nKeyAreaT_px) / p_dScreenH_px;                   // ordinates of the right finger                        [2013-06-28 davidwalley.ca 25C5]
   }else{                                                                                       // Otherwise this is a new touch, so
    bNewSeen = true; idNew = id;                                                                // remember we are seeing a new touch, remember its ID and
    dNewX_1        = (a_motionevent.getX(i) +p_nKeyAreaL_px) / p_dScreenW_px;                   // calculate the co-                                  [2013-06-28 davidwalley.ca 0BC3]
    dNewY_1        = (a_motionevent.getY(i) +p_nKeyAreaT_px) / p_dScreenH_px;                   // ordinates of the new touch                        [2013-06-28 davidwalley.ca 25C5]
  }}//if//for i--                                                                               // .                            [2013-06-28 davidwalley.ca 9026]
                                                                                                //
  p_bPanTouching = bPanSeen;                                                                    // Remember if the left finger is touching the screen.
  p_bKeyTouching = bKeySeen;                                                                    // Remember if the right finger is touching the screen.

  if( p_bPanTouching ){                                                                        // If Pan finger is touching...
   ///    +---------------------------------------+  \
   ///    |    1    |    2    |    3    |    4    |  |
   ///    +---------------------------------------+  |
   ///    |    2    |         |         |         |  |_ Key presses (handled elsewhere).
   ///    +---------------------------------------+  |
   ///    |    3    |         |         |         |  |
   ///    +---------------------------------------+  |
   ///    | Keyboard                              |  /
   ///    | pan to   /                Space bar   |
   ///    | right.  /        /                    |
   ///    | = 1   / Center /                      |
   ///    |      / = 2  /                         |
   ///    |    /     /                            |
   ///    |   /   /   Keyboard                    |
   ///    | /  /  pan to left.                    |
   ///    |//    = 3                              |
   ///    +---------------------------------------+
   ///    |   Keyboard area also accepts press?   |
   ///    +---------------------------------------+
   if( 0.f < p_dTouchPanY_px ){ p_isPanAt = 3;                                                // If in key area, then
   }else{
    float                       dRun_px                 =
        ( p_bRightHanded )?p_dTouchPanX_px :(p_dScreenH_px - p_dTouchPanX_px);              //
    if( dRun_px < 0.01f ){      p_isPanAt = 1;                                              // Avoid division by zero by reporting zone 1.
    }else{
     float                      dSlope                  = -p_dTouchPanY_px / dRun_px;       // Calculate the slope.
     if(       dSlope < 0.5f ){ p_isPanAt = 3;
     }else if( dSlope < 1.5f ){ p_isPanAt = 2;
     }else{                     p_isPanAt = 1;
   }}}//if//if//if                                                                          // .
   sDEBUG0 = "P:"+ p_isPanAt;

   if( ! p_bKeyTouching ){                                                                  // but KEY finger is not, then
    if( bNewSeen ){                                                                         // but a new touch-point was seen, then PAN key is returning, so...
     p_bKeyTouching = true; p_idTouchKey = idNew;                                           // Assign the new touch-point to the KEY finger
     p_dTouchKeyX_1 = dNewX_1; p_dTouchKeyY_1 = dNewY_1;                                    // and remember its position.
   }}//if//if                                                                               // .

  }else{                                                                                    // But if the PAN key was not touching,
   if( bNewSeen ){                                                                          // If a new touch-point was seen, then
    p_bPanTouching = true; p_idTouchPan = idNew;                                            // Assign the new touch-point to the PAN finger
    p_dTouchPanX_px = dNewX_1; p_dTouchPanY_px = dNewY_1;                                   // and remember its position.
    p_bRightHanded = ( p_dTouchPanX_px < 0.5f );                                            // FIRST TOUCH. Remember if left or right handed
  }}//if//if                                                                                // .
                                                                                            //
  if( p_bPanTouching   ||   p_bKeyTouching ){                                               // if something is touching display, then
   p_isState = p_isPANdOWN;
  }else{                                                                                    // but if the right thumb is down, then
   if( p_isINTRO != p_isState ){ p_isState = p_isCONFIRM; }                                 // Reset the state to just starting (with intro message only if nothing has been done yet).
  }//if                                                                                     // .

  invalidate();                                                                             // Schedule a repaint.        [2013-06-28 davidwalley.ca 38DD]
 return true;                                                                               // Report we have handled this event, and we are done.
 }///onTouchEvent/////////////////////////////////////////////////////////////////////////////


 private void                   //////////////////////////////////////////////////////////////
                                p_FindDisplaySize(//                                        // Find and remember the size of the entire device display.
 ){                             //////////////////////////////////////////////////////////////
  DisplayMetrics                displaymetrics        = getResources().getDisplayMetrics(); // Get info about              [2013-06-28 davidwalley.ca 0EEF]
  p_dScreenW_px = displaymetrics.widthPixels;                                               // the size of the full display screen.    [2013-06-28 davidwalley.ca 5566]
  p_dScreenH_px = displaymetrics.heightPixels;                                              // Android does not get this exactly right, but it is fairly close (because of status bar).    [2013-06-28 davidwalley.ca DD63]
 }///p_FindDisplaySize////////////////////////////////////////////////////////////////////////


 private void                   //////////////////////////////////////////////////////////////
                                p_NewKeyAreaSize(//                                         // Get size of keyboard area, and update resources if there has been a change.
 ){                             //////////////////////////////////////////////////////////////
  float                         dW_px                   = getWidth( );                      // Get the width and
  float                         dH_px                   = getHeight();                      // height of this (the keyboard) area.

  if( dW_px == p_dKeyAreaW_px   &&   dH_px == p_dKeyAreaH_px ){                             // If no change then
 return;                                                                                    // exit gracefully
  }//if                                                                                     // .

  int                           an[]                    = {0,0};                            // Create a buffer for receiving co-ordinates.    [2013-06-28 davidwalley.ca 92CB 2013-06-28 davidwalley.ca]
  getLocationOnScreen(an);                                                                  // Get the top-left position of this keyboard section within the entire screen    [2013-06-28 davidwalley.ca 4942 2013-06-28 davidwalley.ca]
  p_nKeyAreaL_px = an[0]; p_nKeyAreaT_px = an[1];                                           // and remember them in a more obvious format.
  p_dKeyAreaW_px = dW_px; p_dKeyAreaH_px = dH_px;

  p_bitmapFinger_L = p_bitmapResized(dH_px ,dH_px ,R.drawable.fingerprint_l);
  p_bitmapFinger_R = p_bitmapResized(dH_px ,dH_px ,R.drawable.fingerprint_r);
 }///p_NewKeyAreaSize/////////////////////////////////////////////////////////////////////////


 void                           //////////////////////////////////////////////////////////////
                                p_SetupKeyboard(//                                          // Translate a keyboard layout map to an array of critical info (somewhat inefficient, so done once on startup).
 ){                             //////////////////////////////////////////////////////////////
  final String[][]              sLayout                 = {
  ///  0    1    2       3     4    5          6     7    8       9     A     B
   { "esc","Q" ,"W"    ,"E"  ,"R" ,"T"       ,"Y"  ,"U" ,"I"    ,"O"  ,"P"  ,"|"  }         //
  ,{ "~"  ,"A" ,"S"    ,"D"  ,"F" ,"G"       ,"H"  ,"J" ,"K"    ,"L"  ,":"  ,"qut"}         //
  ,{ "`"  ,"Z" ,"X"    ,"C"  ,"V" ,"B"       ,"N"  ,"M" ,"<"    ,">"  ,"?"  ,"   "}         //
                                                                                            //
  ,{ "tab","{" ,"}"    ,"   ","[" ,"]"       ,"   ","+ ","="    ,"ins","del","   "}         //
  ,{ "^^" ,"!" ,"@"    ,"#"  ,"$" ,"%"       ,"^"  ,"&" ,"*"    ,"("  ,")"  ,"_"  }         //
  ,{ "<<" ,"1" ,"2"    ,"3"  ,"4" ,"5"       ,"6"  ,"7" ,"8"    ,"9"  ,"0"  ,"-"  }         //
                                                                                            //
  ,{ ">>" ,"q" ,"w"    ,"e"  ,"r" ,"t"       ,"y"  ,"u" ,"i"    ,"o"  ,"p"  ,"\\" }         //
  ,{ "vv" ,"a" ,"s"    ,"d"  ,"f" ,"g"       ,"h"  ,"j" ,"k"    ,"l"  ,";"  ,"'"  }         //
  ,{ "   ","z" ,"x"    ,"c"  ,"v" ,"b"       ,"n"  ,"m" ,","    ,"."  ,"/"  ,"   "}         //
  };
  //   ??? bak ins
  String                        s;
  for(  int j = 0; j <  9; j++ ){           // Row
   for( int i = 0; i < 12; i++ ){                 // Column
    s = sLayout[j][i];
    if(      "   ".equals(s) ){ p_anAscii[j][i] =  32; p_anShift[j][i] = 0; p_anCode[j][i] =  62; }  // Space
    else if( "###".equals(s) ){ p_anAscii[j][i] =  -1; p_anShift[j][i] = 0; p_anCode[j][i] =   0; }  // Unassigned
    else if( "esc".equals(s) ){ p_anAscii[j][i] =  -2; p_anShift[j][i] = 0; p_anCode[j][i] = 111; }  // escape
    else if( "del".equals(s) ){ p_anAscii[j][i] =  -3; p_anShift[j][i] = 0; p_anCode[j][i] =   4; }  // delete
    else if( "ins".equals(s) ){ p_anAscii[j][i] =  -4; p_anShift[j][i] = 0; p_anCode[j][i] = 124; }  // insert
    else if( "tab".equals(s) ){ p_anAscii[j][i] =  -5; p_anShift[j][i] = 0; p_anCode[j][i] =  61; }  // tab
    else if( "^^" .equals(s) ){ p_anAscii[j][i] =  -6; p_anShift[j][i] = 0; p_anCode[j][i] =  19; }  // up arrow
    else if( "<<" .equals(s) ){ p_anAscii[j][i] =  -7; p_anShift[j][i] = 0; p_anCode[j][i] =  21; }  // left arrow
    else if( ">>" .equals(s) ){ p_anAscii[j][i] =  -8; p_anShift[j][i] = 0; p_anCode[j][i] =  22; }  // right arrow
    else if( "vv" .equals(s) ){ p_anAscii[j][i] =  -9; p_anShift[j][i] = 0; p_anCode[j][i] =  20; }  // down arrow
    else if( "!"  .equals(s) ){ p_anAscii[j][i] =  33; p_anShift[j][i] = 1; p_anCode[j][i] =   8; }
    else if( "qut".equals(s) ){ p_anAscii[j][i] =  34; p_anShift[j][i] = 1; p_anCode[j][i] =  75; }  // double quote
    else if( "#"  .equals(s) ){ p_anAscii[j][i] =  35; p_anShift[j][i] = 0; p_anCode[j][i] =  18; }
    else if( "$"  .equals(s) ){ p_anAscii[j][i] =  36; p_anShift[j][i] = 1; p_anCode[j][i] =  11; }
    else if( "%"  .equals(s) ){ p_anAscii[j][i] =  37; p_anShift[j][i] = 1; p_anCode[j][i] =  12; }
    else if( "&"  .equals(s) ){ p_anAscii[j][i] =  38; p_anShift[j][i] = 1; p_anCode[j][i] =  14; }
    else if( "'"  .equals(s) ){ p_anAscii[j][i] =  39; p_anShift[j][i] = 0; p_anCode[j][i] =  75; }
    else if( "("  .equals(s) ){ p_anAscii[j][i] =  40; p_anShift[j][i] = 1; p_anCode[j][i] =  16; }
    else if( ")"  .equals(s) ){ p_anAscii[j][i] =  41; p_anShift[j][i] = 1; p_anCode[j][i] =   7; }
    else if( "*"  .equals(s) ){ p_anAscii[j][i] =  42; p_anShift[j][i] = 0; p_anCode[j][i] =  17; }
    else if( "+"  .equals(s) ){ p_anAscii[j][i] =  43; p_anShift[j][i] = 0; p_anCode[j][i] =  81; }
    else if( ","  .equals(s) ){ p_anAscii[j][i] =  44; p_anShift[j][i] = 0; p_anCode[j][i] =  55; }
    else if( "-"  .equals(s) ){ p_anAscii[j][i] =  45; p_anShift[j][i] = 0; p_anCode[j][i] =  69; }
    else if( "."  .equals(s) ){ p_anAscii[j][i] =  46; p_anShift[j][i] = 0; p_anCode[j][i] =  56; }
    else if( "/"  .equals(s) ){ p_anAscii[j][i] =  47; p_anShift[j][i] = 0; p_anCode[j][i] =  76; }
    else if( "0"  .equals(s) ){ p_anAscii[j][i] =  48; p_anShift[j][i] = 0; p_anCode[j][i] =   7; }
    else if( "1"  .equals(s) ){ p_anAscii[j][i] =  49; p_anShift[j][i] = 0; p_anCode[j][i] =   8; }
    else if( "2"  .equals(s) ){ p_anAscii[j][i] =  50; p_anShift[j][i] = 0; p_anCode[j][i] =   9; }
    else if( "3"  .equals(s) ){ p_anAscii[j][i] =  51; p_anShift[j][i] = 0; p_anCode[j][i] =  10; }
    else if( "4"  .equals(s) ){ p_anAscii[j][i] =  52; p_anShift[j][i] = 0; p_anCode[j][i] =  11; }
    else if( "5"  .equals(s) ){ p_anAscii[j][i] =  53; p_anShift[j][i] = 0; p_anCode[j][i] =  12; }
    else if( "6"  .equals(s) ){ p_anAscii[j][i] =  54; p_anShift[j][i] = 0; p_anCode[j][i] =  13; }
    else if( "7"  .equals(s) ){ p_anAscii[j][i] =  55; p_anShift[j][i] = 0; p_anCode[j][i] =  14; }
    else if( "8"  .equals(s) ){ p_anAscii[j][i] =  56; p_anShift[j][i] = 0; p_anCode[j][i] =  15; }
    else if( "9"  .equals(s) ){ p_anAscii[j][i] =  57; p_anShift[j][i] = 0; p_anCode[j][i] =  16; }
    else if( ":"  .equals(s) ){ p_anAscii[j][i] =  58; p_anShift[j][i] = 1; p_anCode[j][i] =  74; }
    else if( ";"  .equals(s) ){ p_anAscii[j][i] =  59; p_anShift[j][i] = 0; p_anCode[j][i] =  74; }
    else if( "<"  .equals(s) ){ p_anAscii[j][i] =  60; p_anShift[j][i] = 1; p_anCode[j][i] =  55; } // ?
    else if( "="  .equals(s) ){ p_anAscii[j][i] =  61; p_anShift[j][i] = 0; p_anCode[j][i] =  70; }
    else if( ">"  .equals(s) ){ p_anAscii[j][i] =  62; p_anShift[j][i] = 1; p_anCode[j][i] =  56; }
    else if( "?"  .equals(s) ){ p_anAscii[j][i] =  63; p_anShift[j][i] = 1; p_anCode[j][i] =  76; }
    else if( "@"  .equals(s) ){ p_anAscii[j][i] =  64; p_anShift[j][i] = 1; p_anCode[j][i] =  77; }
    else if( "A"  .equals(s) ){ p_anAscii[j][i] =  65; p_anShift[j][i] = 1; p_anCode[j][i] =  29; }
    else if( "B"  .equals(s) ){ p_anAscii[j][i] =  66; p_anShift[j][i] = 1; p_anCode[j][i] =  30; }
    else if( "C"  .equals(s) ){ p_anAscii[j][i] =  67; p_anShift[j][i] = 1; p_anCode[j][i] =  31; }
    else if( "D"  .equals(s) ){ p_anAscii[j][i] =  68; p_anShift[j][i] = 1; p_anCode[j][i] =  32; }
    else if( "E"  .equals(s) ){ p_anAscii[j][i] =  69; p_anShift[j][i] = 1; p_anCode[j][i] =  33; }
    else if( "F"  .equals(s) ){ p_anAscii[j][i] =  70; p_anShift[j][i] = 1; p_anCode[j][i] =  34; }
    else if( "G"  .equals(s) ){ p_anAscii[j][i] =  71; p_anShift[j][i] = 1; p_anCode[j][i] =  35; }
    else if( "H"  .equals(s) ){ p_anAscii[j][i] =  72; p_anShift[j][i] = 1; p_anCode[j][i] =  36; }
    else if( "I"  .equals(s) ){ p_anAscii[j][i] =  73; p_anShift[j][i] = 1; p_anCode[j][i] =  37; }
    else if( "J"  .equals(s) ){ p_anAscii[j][i] =  74; p_anShift[j][i] = 1; p_anCode[j][i] =  38; }
    else if( "K"  .equals(s) ){ p_anAscii[j][i] =  75; p_anShift[j][i] = 1; p_anCode[j][i] =  39; }
    else if( "L"  .equals(s) ){ p_anAscii[j][i] =  76; p_anShift[j][i] = 1; p_anCode[j][i] =  40; }
    else if( "M"  .equals(s) ){ p_anAscii[j][i] =  77; p_anShift[j][i] = 1; p_anCode[j][i] =  41; }
    else if( "N"  .equals(s) ){ p_anAscii[j][i] =  78; p_anShift[j][i] = 1; p_anCode[j][i] =  42; }
    else if( "O"  .equals(s) ){ p_anAscii[j][i] =  79; p_anShift[j][i] = 1; p_anCode[j][i] =  43; }
    else if( "P"  .equals(s) ){ p_anAscii[j][i] =  80; p_anShift[j][i] = 1; p_anCode[j][i] =  44; }
    else if( "Q"  .equals(s) ){ p_anAscii[j][i] =  81; p_anShift[j][i] = 1; p_anCode[j][i] =  45; }
    else if( "R"  .equals(s) ){ p_anAscii[j][i] =  82; p_anShift[j][i] = 1; p_anCode[j][i] =  46; }
    else if( "S"  .equals(s) ){ p_anAscii[j][i] =  83; p_anShift[j][i] = 1; p_anCode[j][i] =  47; }
    else if( "T"  .equals(s) ){ p_anAscii[j][i] =  84; p_anShift[j][i] = 1; p_anCode[j][i] =  48; }
    else if( "U"  .equals(s) ){ p_anAscii[j][i] =  85; p_anShift[j][i] = 1; p_anCode[j][i] =  49; }
    else if( "V"  .equals(s) ){ p_anAscii[j][i] =  86; p_anShift[j][i] = 1; p_anCode[j][i] =  50; }
    else if( "W"  .equals(s) ){ p_anAscii[j][i] =  87; p_anShift[j][i] = 1; p_anCode[j][i] =  51; }
    else if( "X"  .equals(s) ){ p_anAscii[j][i] =  88; p_anShift[j][i] = 1; p_anCode[j][i] =  52; }
    else if( "Y"  .equals(s) ){ p_anAscii[j][i] =  89; p_anShift[j][i] = 1; p_anCode[j][i] =  53; }
    else if( "Z"  .equals(s) ){ p_anAscii[j][i] =  90; p_anShift[j][i] = 1; p_anCode[j][i] =  54; }
    else if( "["  .equals(s) ){ p_anAscii[j][i] =  91; p_anShift[j][i] = 0; p_anCode[j][i] =  71; }
    else if( "\\" .equals(s) ){ p_anAscii[j][i] =  92; p_anShift[j][i] = 0; p_anCode[j][i] =  73; } // back slash
    else if( "]"  .equals(s) ){ p_anAscii[j][i] =  93; p_anShift[j][i] = 0; p_anCode[j][i] =  72; }
    else if( "^"  .equals(s) ){ p_anAscii[j][i] =  94; p_anShift[j][i] = 1; p_anCode[j][i] =  13; }
    else if( "_"  .equals(s) ){ p_anAscii[j][i] =  95; p_anShift[j][i] = 1; p_anCode[j][i] =  69; }
    else if( "`"  .equals(s) ){ p_anAscii[j][i] =  96; p_anShift[j][i] = 0; p_anCode[j][i] =  68; }
    else if( "a"  .equals(s) ){ p_anAscii[j][i] =  97; p_anShift[j][i] = 0; p_anCode[j][i] =  29; }
    else if( "b"  .equals(s) ){ p_anAscii[j][i] =  98; p_anShift[j][i] = 0; p_anCode[j][i] =  30; }
    else if( "c"  .equals(s) ){ p_anAscii[j][i] =  99; p_anShift[j][i] = 0; p_anCode[j][i] =  31; }
    else if( "d"  .equals(s) ){ p_anAscii[j][i] = 100; p_anShift[j][i] = 0; p_anCode[j][i] =  32; }
    else if( "e"  .equals(s) ){ p_anAscii[j][i] = 101; p_anShift[j][i] = 0; p_anCode[j][i] =  33; }
    else if( "f"  .equals(s) ){ p_anAscii[j][i] = 102; p_anShift[j][i] = 0; p_anCode[j][i] =  34; }
    else if( "g"  .equals(s) ){ p_anAscii[j][i] = 103; p_anShift[j][i] = 0; p_anCode[j][i] =  35; }
    else if( "h"  .equals(s) ){ p_anAscii[j][i] = 104; p_anShift[j][i] = 0; p_anCode[j][i] =  36; }
    else if( "i"  .equals(s) ){ p_anAscii[j][i] = 105; p_anShift[j][i] = 0; p_anCode[j][i] =  37; }
    else if( "j"  .equals(s) ){ p_anAscii[j][i] = 106; p_anShift[j][i] = 0; p_anCode[j][i] =  38; }
    else if( "k"  .equals(s) ){ p_anAscii[j][i] = 107; p_anShift[j][i] = 0; p_anCode[j][i] =  39; }
    else if( "l"  .equals(s) ){ p_anAscii[j][i] = 108; p_anShift[j][i] = 0; p_anCode[j][i] =  40; }
    else if( "m"  .equals(s) ){ p_anAscii[j][i] = 109; p_anShift[j][i] = 0; p_anCode[j][i] =  41; }
    else if( "n"  .equals(s) ){ p_anAscii[j][i] = 110; p_anShift[j][i] = 0; p_anCode[j][i] =  42; }
    else if( "o"  .equals(s) ){ p_anAscii[j][i] = 111; p_anShift[j][i] = 0; p_anCode[j][i] =  43; }
    else if( "p"  .equals(s) ){ p_anAscii[j][i] = 112; p_anShift[j][i] = 0; p_anCode[j][i] =  44; }
    else if( "q"  .equals(s) ){ p_anAscii[j][i] = 113; p_anShift[j][i] = 0; p_anCode[j][i] =  45; }
    else if( "r"  .equals(s) ){ p_anAscii[j][i] = 114; p_anShift[j][i] = 0; p_anCode[j][i] =  46; }
    else if( "s"  .equals(s) ){ p_anAscii[j][i] = 115; p_anShift[j][i] = 0; p_anCode[j][i] =  47; }
    else if( "t"  .equals(s) ){ p_anAscii[j][i] = 116; p_anShift[j][i] = 0; p_anCode[j][i] =  48; }
    else if( "u"  .equals(s) ){ p_anAscii[j][i] = 117; p_anShift[j][i] = 0; p_anCode[j][i] =  49; }
    else if( "v"  .equals(s) ){ p_anAscii[j][i] = 118; p_anShift[j][i] = 0; p_anCode[j][i] =  50; }
    else if( "w"  .equals(s) ){ p_anAscii[j][i] = 119; p_anShift[j][i] = 0; p_anCode[j][i] =  51; }
    else if( "x"  .equals(s) ){ p_anAscii[j][i] = 120; p_anShift[j][i] = 0; p_anCode[j][i] =  52; }
    else if( "y"  .equals(s) ){ p_anAscii[j][i] = 121; p_anShift[j][i] = 0; p_anCode[j][i] =  53; }
    else if( "z"  .equals(s) ){ p_anAscii[j][i] = 122; p_anShift[j][i] = 0; p_anCode[j][i] =  54; }
    else if( "{"  .equals(s) ){ p_anAscii[j][i] = 123; p_anShift[j][i] = 1; p_anCode[j][i] =  71; }
    else if( "|"  .equals(s) ){ p_anAscii[j][i] = 124; p_anShift[j][i] = 1; p_anCode[j][i] =  73; }
    else if( "}"  .equals(s) ){ p_anAscii[j][i] = 125; p_anShift[j][i] = 1; p_anCode[j][i] =  72; }
    else if( "~"  .equals(s) ){ p_anAscii[j][i] = 126; p_anShift[j][i] = 1; p_anCode[j][i] =  68; }
  }}//for i0//for j
 }////p_SetupKeyboard()///////////////////////////////////////////////////////////////////////////


 private android.graphics.Bitmap ///keyboardview_pankey.////////////////////////////////////////// Define a procedure to:   [2012-04-09 dave@davewalley.ca F19A 2012-07-13 davidwalley.ca]
                                 p_bitmapResized(//                                             // Create a bitmap of a desired size,   [2012-05-21 davidwalley.ca F7D9 2012-10-07 davidwalley.ca]
  final float                     a_fW                                                          // given a width and        [2012-05-21 davidwalley.ca B907 2012-09-22 davidwalley.ca]
 ,final float                     a_fH                                                          // height for the new bitmap,   [2012-05-21 davidwalley.ca 43FB 2012-09-22 davidwalley.ca]
 ,final int                       a_isResourceId                                                // based on this bitmap from the resources (in the /res sub-directory).   [2012-05-21 davidwalley.ca D9F8 2012-09-22 davidwalley.ca]
 ){                              ///////////////////////////////////////////////////////////////// Report with the new bitmap.   [2012-04-09 dave@davewalley.ca 0D19]
  android.graphics.Bitmap        r_bitmap               = null;                                 // Need a utility image buffer.   [2012-04-05 dave@davewalley.ca F1FD 2012-05-21 davidwalley.ca]
  android.graphics.Bitmap        bitmapResource         = null;                                 // Need a utility image buffer.   [2012-04-05 dave@davewalley.ca 5411 2012-10-07 davidwalley.ca]
                                                                                                //
  bitmapResource = android.graphics.BitmapFactory.decodeResource(                               // get the background image from   [2012-04-05 dave@davewalley.ca 24EF 2012-10-07 davidwalley.ca]
   getResources() ,a_isResourceId                                                               // the resource             [2012-04-05 dave@davewalley.ca 9FD2 2012-05-21 davidwalley.ca]
  );                                                                                            // .                        [2012-04-05 dave@davewalley.ca 0D59 2012-05-21 davidwalley.ca]
  r_bitmap = android.graphics.Bitmap.createBitmap(                                              // Create the background image buffer,   [2012-04-06 dave@davewalley.ca 8992 2012-05-21 davidwalley.ca]
   (int)a_fW ,(int)a_fH ,android.graphics.Bitmap.Config.ARGB_8888                               // to fit the desired size   [2012-04-06 dave@davewalley.ca D5C7 2012-10-07 davidwalley.ca]
  );                                                                                            // .                        [2012-04-06 dave@davewalley.ca 0D59]
  android.graphics.Canvas       canvas             = new android.graphics.Canvas(r_bitmap);     // Create a drawing object for the background image buffer.   [2012-04-06 dave@davewalley.ca AC71 2012-05-21 davidwalley.ca]
                                                                                                //
  canvas.drawBitmap(bitmapResource                                                              // Draw the background graphic   [2012-04-06 dave@davewalley.ca 3A22 2012-10-07 davidwalley.ca]
  ,new android.graphics.Rect( 0 ,0 ,bitmapResource.getWidth() ,bitmapResource.getHeight() )     // to fit                   [2012-04-06 dave@davewalley.ca 5048 2012-10-07 davidwalley.ca]
  ,new android.graphics.Rect( 0 ,0 ,(int)a_fW                 ,(int)a_fH                  )     // the buffer               [2012-04-06 dave@davewalley.ca 9939 2012-05-21 davidwalley.ca]
  ,null                                                                                         // "                        [2012-04-06 dave@davewalley.ca AE92]
  );                                                                                            // .                        [2012-04-06 dave@davewalley.ca 0D59]
 return r_bitmap;                                                                               // Report the new bitmap.   [2012-05-21 davidwalley.ca B503]
 }///p_bitmapResized////////////////////////////////////////////////////////////////////////////// End of this procedure.   [2012-05-21 davidwalley.ca A448 2012-10-07 davidwalley.ca]
                                                                                                //
                                                                                                //
 private void                   //////////////////////////////////////////////////////////////////
                                p_SendToUserApp(//                                              // Helper to send a key down / key up pair to the client editor application.
  final int                      a_iColumn                                                      //
 ,final int                      a_iRow                                                         //
 ){                             //////////////////////////////////////////////////////////////////
  ((PanKey)getOnKeyboardActionListener()).SendKeySequence(                                      //
   p_anCode[ a_iRow][a_iColumn]                                                                 //
  ,p_anShift[a_iRow][a_iColumn]                                                                 // 0 = normal, 1 = shifted, 2 = modifier, 4 = custom
  );                                                                                            //
 }///p_SendToUserApp//////////////////////////////////////////////////////////////////////////////

}/// class KeyboardView_PanKey////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////
                                                                                                //
                                                                                                //
                                                                                                //
                                                                                                //
class ////////////////////////////////////////////////////////////////////////////////////////////
                                View_Popup ///////////////////////////////////////////////////////
extends            android.view.View /////////////////////////////////////////////////////////////
{/////////////////////////////////////////////////////////////////////////////////////////////////
                                                                                                //
 private static final String    sTAG                    = "PanKey";                         // For logging.

 private KeyboardView_PanKey    p_keyboardview_pankeyMom = null;

 public                         //////////////////////////////////////////////////////////////////
                                View_Popup(//                                                   //
  KeyboardView_PanKey            a_keyboardview_pankeyMom
 ,android.content.Context        a_context                                                      //
 ){                             //////////////////////////////////////////////////////////////////
  super(a_context);                                                                             //
  Log.d(sTAG,"View_Popup.View_Popup 1 Construct");
  p_keyboardview_pankeyMom = a_keyboardview_pankeyMom;
 }////////////////////////////////////////////////////////////////////////////////////////////////
                                                                                                //
                                                                                                //
 private android.graphics.Paint   p_paint               = new android.graphics.Paint();         // General purpose paint object for drawing on screen.                            [2013-06-28 davidwalley.ca 710B]
                                                                                                //
 public void                    //////////////////////////////////////////////////////////////////
                                onDraw(//                                                       //
  android.graphics.Canvas        a_canvas                                                       //
 ){                             //////////////////////////////////////////////////////////////////
  Log.d(sTAG,"View_Popup.onDraw 1");
  p_keyboardview_pankeyMom.DrawPopup(a_canvas);
 }//onDraw////////////////////////////////////////////////////////////////////////////////////////
                                                                                                //
}//class View_Popup///////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////
                                                                                                //
                                                                                                //
//End of file.
