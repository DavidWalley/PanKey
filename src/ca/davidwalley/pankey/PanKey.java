// PanKey.java - Main class for custom user input service, replacing stock keyboard.
// (c) 2013 David C. Walley
// This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/.

package ca.davidwalley.pankey;                                                              // This code is part of a package created by David Walley, owner of davidwalley.ca.

import android.util.*;                                                                      // For ease of debug logging.

// In the Android system, an IME is an Android application that contains a special IME service.
// The application's manifest file must declare the service, request the necessary permissions,
// provide an intent filter that matches the action action.view.InputMethod, and
// provide metadata that defines characteristics of the IME.
// In addition, to provide a settings interface that allows the user to modify the behavior of the IME,
// you can define a "settings" activity that can be launched from System Settings.
//   [http://developer.android.com/guide/topics/text/creating-input-method.html]

// AndroidManifest.xml:
// <manifest xmlns:android="http://schemas.android.com/apk/res/android" package="ca.davidwalley.pankey">
//  <application android:label="@string/ime_name">
//   <service                                                                             // Declare the service, and
//    android:name="PanKey"
//    android:label="PanKey"
//    android:permission="android.permission.BIND_INPUT_METHOD"                           // allow it to connect the IME to the system
//   >
//    <intent-filter>                                                               // Sets up an intent filter
//     <action android:name="android.view.InputMethod" />                           // that matches the action android.view.InputMethod,
//    </intent-filter>                                                              // and
//    <meta-data android:name="android.view.im" android:resource="@xml/method" />   // defines metadata for the IME.
//   </service>
//  </application>
// </manifest>

// method.xml:
// <?xml version="1.0" encoding="utf-8"?>
// <input-method xmlns:android="http://schemas.android.com/apk/res/android" />

// layout.xml
// <?xml version="1.0" encoding="utf-8"?>
// <ca.davidwalley.pankey.KeyboardView_PanKey
//  xmlns:android="http://schemas.android.com/apk/res/android"
//  android:id="@+id/keyboard"
//  android:layout_alignParentBottom="true"
//  android:layout_width="match_parent"
//  android:layout_height="wrap_content"
// />

// qwerty.xml
// <?xml version="1.0" encoding="utf-8"?>
// <Keyboard
//  xmlns:android="http://schemas.android.com/apk/res/android"
//  android:keyWidth="100%p"
//  android:horizontalGap="0px"
//  android:verticalGap="0px"
//  android:keyHeight="15%"
// >
//  <Row>
//   <Key android:codes="113" android:keyLabel="q" android:keyEdgeFlags="left"/>
//  </Row>
// </Keyboard>

// strings.xml
// <?xml version="1.0" encoding="utf-8"?>
// <resources xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">
//  <string name="ime_name">PanKey</string>
//  <string name="label_go_key">Go</string>
//  <string name="label_next_key">Next</string>
//  <string name="label_send_key">Send</string>
// </resources>


// Lifecycle of Input Method Service: http://developer.android.com/guide/topics/text/creating-input-method.html
//  onCreate()
//  onCreateInputView()
//  onCreateCandidateViews()
//  onStartInputView()              <-------------------------+
//   Text input gets the current input method subtype.<---+   |
//   InputMethodManager#getCurrentInputMethodSubtype()    |   |
//   Text input has started.                              |   |
//  onCurrentInputMethodChanged()                         |   |
//   Look for a change in the subtype --------------------+   |
//  onFinishInput()                                           |
//   move to an additional field  ----------------------------+
//  onDestroy()


public class /////////////////////////////////////////////////////////////////////////////////
                                PanKey ///////////////////////////////////////////////////////
extends    android.inputmethodservice.InputMethodService ///////////////////////////////////// a standard implementation of an InputMethod, which we derive from and customize.
// extends android.app.Service /////////////////////////////////////////////////////////////// Android runs this as a service (rather than an application).
implements android.inputmethodservice.KeyboardView.OnKeyboardActionListener ////////////////// Listener for virtual keyboard events, such as keypresses or swipes. We must implement the inherited abstract methods.
{/////////////////////////////////////////////////////////////////////////////////////////////

 private static final String    sTAG                    = "PanKey";                         // For logging.

 @Override public void          ////////////////////////////////////////////////////////////// Called by the framework when
                                onCreate(//                                                 // Main initialization of this input method component.  Be sure to call to super class.
 ){                             //////////////////////////////////////////////////////////////
  super.onCreate();                                                                         //
  Log.d(sTAG,"PanKey.onCreate 1");
 }///onCreate/////////////////////////////////////////////////////////////////////////////////


// private android.inputmethodservice.KeyboardView    p_keyboardview;                         // A view that renders a virtual Keyboard. It handles rendering of keys and detecting key presses and touch movements.
 private KeyboardView_PanKey    p_keyboardview          = null;                         // A view that renders a virtual Keyboard. It handles rendering of keys and detecting key presses and touch movements.


 @Override public android.view.View ////////////////////////////////////////////////////////// Called by the framework when
                                onCreateInputView(//                                        // your View needs to be generated. Called the first time your input method is displayed, and every time it needs to be re-created (e.g. configuration change).
 ){                             //////////////////////////////////////////////////////////////
  Log.d(sTAG,"PanKey.onCreateInputView 1");
  p_keyboardview =
//                   (android.inputmethodservice.KeyboardView)                                //
                     (KeyboardView_PanKey)
                                         getLayoutInflater().inflate(R.layout.input, null); // Layout is basically ignored, but this works to reserve a percentage of the screen, so I use it.
  p_keyboardview.setKeyboard(p_keyboardQwerty);                                             // Attaches a keyboard to this view. The keyboard can be switched at any time and the view will re-layout itself to accommodate the keyboard.

  p_keyboardview.setOnKeyboardActionListener(this);                                         // The keyboard view sends info back to this object.
 return p_keyboardview;                                                                     //
 }///onCreateInputView////////////////////////////////////////////////////////////////////////


//                              onCreateCandidateViews()                                    // If showing potential words, create a view for this (we don't yet).


 @Override public void          ////////////////////////////////////////////////////////////// Called by the framework when
                                onStartInputView(//                                         // the input view is being shown and input has started on a new editor. View-specific setup here. You are guaranteed that onCreateInputView() will have been called some time before this function is called.
  android.view.inputmethod.EditorInfo   a_editorinfo                                        // Description of the type of text being edited.
 ,boolean                               a_bRestart                                          // Set to true if we are restarting input on the same text field as before.
 ){                             //////////////////////////////////////////////////////////////
  super.onStartInputView(a_editorinfo, a_bRestart);                                         //
  Log.d(sTAG,"PanKey.onStartInputView");
  p_keyboardview.closing();                                                                 // Dismisses any popups ?
 }///onStartInputView/////////////////////////////////////////////////////////////////////////


//                              onCurrentInputMethodChanged()


 @Override public void          ////////////////////////////////////////////////////////////// Called by the framework when
                                onFinishInput(//                                            // the user is done editing a field.  We can use this to reset our state.
 ){                             //////////////////////////////////////////////////////////////
  super.onFinishInput();                                                                    //
  Log.d(sTAG,"PanKey.onFinishInput");
  if( null != p_keyboardview ){
   p_keyboardview.CleanUp();
   p_keyboardview.closing();          // Dismisses any popups ?
  }//if
 }///onFinishInput////////////////////////////////////////////////////////////////////////////


//                              onDestroy()


// For android.inputmethodservice.KeyboardView.OnKeyboardActionListener:
 @Override public void          swipeRight(           ){ }
 @Override public void          swipeLeft(            ){ }
 @Override public void          swipeDown(            ){ }
 @Override public void          swipeUp(              ){ }                                  // Do nothing.
 @Override public void          onPress(  int a_isKey ){ }                                  // Do nothing.
 @Override public void          onRelease(int a_isKey ){ }                                  // Do nothing.
 @Override public void          onText(CharSequence ac){ }

 private long                   p_longMetaKeys          = 0;
 private int                    p_isType                = 0;

 private int                    p_nDisplayW;
 private android.inputmethodservice.Keyboard    p_keyboardQwerty;                           // Loads an XML description of a keyboard and stores the attributes of the keys.

 @Override public void          ////////////////////////////////////////////////////////////// Called by the framework after
                                onInitializeInterface(//                                    // Creation and Configuration changes - for user-interface setup. Configuration changes can happen after the keyboard gets recreated, so we need to be able to re-build the keyboards if the available space has changed.
 ){                             //////////////////////////////////////////////////////////////
  Log.d(sTAG,"PanKey.onInitializeInterface");
  if( null != p_keyboardview ){                                                             // If we have created the main keyboard, then
   p_keyboardview.CleanUp();                                                                // Clean up, including dismissing any popup window over the keyboard.
   int                          nMaxWidth               = getMaxWidth();                    // get the available space to fit the keyboards.
   if( nMaxWidth == p_nDisplayW ){ return; }                                                // If the width has not changed then there is nothing else to be done.
   p_nDisplayW = nMaxWidth;                                                                 // Remember the width for future comparison.
  }//if

  p_keyboardQwerty = new android.inputmethodservice.Keyboard( this ,R.xml.qwerty );
 }///onInitializeInterface////////////////////////////////////////////////////////////////////


 @Override public void          ////////////////////////////////////////////////////////////// Called by the framework when
                                onStartInput(//                                             // we have been bound to a client editor application and are receiving all of the detailed information about the target of our edits, we can initialize the input method to begin operating on an application.
  android.view.inputmethod.EditorInfo   a_editorinfo                                        // describes several attributes of a text editing object that an input method is communicating with (typically an EditText), most importantly the type of text content it contains.
 ,boolean                               a_bRestart
 ){                             //////////////////////////////////////////////////////////////
  super.onStartInput(a_editorinfo, a_bRestart);
  Log.d(sTAG,"PanKey.onStartInput");

  if( ! a_bRestart ){ p_longMetaKeys = 0; }                                                 // Clear meta-key shift states.

  p_isType = 0;                                                                             // Default is a text input.
  switch(    android.view.inputmethod.EditorInfo.TYPE_MASK_CLASS     & a_editorinfo.inputType ){ // We are now going to initialize our state based on the type of text being edited.
  case       android.view.inputmethod.EditorInfo.TYPE_CLASS_NUMBER  :                         p_isType = 1;
  break;case android.view.inputmethod.EditorInfo.TYPE_CLASS_DATETIME:                         p_isType = 2;
  break;case android.view.inputmethod.EditorInfo.TYPE_CLASS_PHONE   :                         p_isType = 3;
  break;case android.view.inputmethod.EditorInfo.TYPE_CLASS_TEXT    :
   int      is =  android.view.inputmethod.EditorInfo.TYPE_MASK_VARIATION & a_editorinfo.inputType;
   if(      is == android.view.inputmethod.EditorInfo.TYPE_TEXT_VARIATION_PASSWORD         ){ p_isType = 4; }
   else if( is == android.view.inputmethod.EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ){ p_isType = 5; } // "
   else if( is == android.view.inputmethod.EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS    ){ p_isType = 6; } // or email
   else if( is == android.view.inputmethod.EditorInfo.TYPE_TEXT_VARIATION_URI              ){ p_isType = 7; } // or URL
   else if( is == android.view.inputmethod.EditorInfo.TYPE_TEXT_VARIATION_FILTER           ){ p_isType = 8; } // or ???
  }//switch

  //p_latinkeyboardCurrent.setImeOptions(getResources(), a_editorinfo.imeOptions);          // Update the label on the enter key, depending on what the application says it will do.
 }///onStartInput/////////////////////////////////////////////////////////////////////////////


 @Override public boolean       ////////////////////////////////////////////////////////////// Called by the framework when
                                onKeyDown(//                                                // key events being delivered to the client editor application.
                                                                                            // We get first crack at them, and can either resume them or let the framework forward them to the client editor application.
  int                            a_isKeyCode
 ,android.view.KeyEvent          a_keyevent
 ){                             ////////////////////////////////////////////////////////////// Report true if event was handled, false if the event was not 'consumed' (fully handled here).
 return super.onKeyDown(a_isKeyCode, a_keyevent);                                           // The default is to let the parent class code of framework handle the keypress.
 }///onKeyDown////////////////////////////////////////////////////////////////////////////////


 @Override public void          //////////////////////////////////////////////////////////////
                                onKey(//                                                    // Handle key press message from the KeyboardView?
  int                            a_isKey                                                    //
 ,int[]                          a_aisKeyCodes                                              //
 ){                             //////////////////////////////////////////////////////////////
  getCurrentInputConnection().sendKeyEvent(
   new android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN ,a_isKey)
  );
  getCurrentInputConnection().sendKeyEvent(
   new android.view.KeyEvent(android.view.KeyEvent.ACTION_UP   ,a_isKey)
  );
 }///onKey/////////////////////////////////////////////////////////////////////////////


 public void                    //////////////////////////////////////////////////////////////
                                SendKeySequence(//                                          //
  int                            a_isKeyCode                                                //
 ,int                            a_isKeyShift                                               //
 ){                             //////////////////////////////////////////////////////////////
  android.view.inputmethod.InputConnection               inputconnection         = getCurrentInputConnection();
  long                          whenEvent               = android.os.SystemClock.uptimeMillis();

// int FLAG_CANCELED             When associated with key up events, this indicates that the key press has been canceled.
// int FLAG_CANCELED_LONG_PRESS  Set when a key event has FLAG_CANCELED set because a long press action was executed while it was down.
// int FLAG_FALLBACK             Set when a key event has been synthesized to implement default behavior for an event that the application did not handle.
// int FLAG_FROM_SYSTEM          Set if an event was known to come from a trusted part of the system.
// int FLAG_LONG_PRESS           Set for the first key repeat that occurs after the long press timeout.
// int FLAG_TRACKING             Set for ACTION_UP when this event's key code is still being tracked from its initial down.
// int FLAG_VIRTUAL_HARD_KEY     Key event was generated by a virtual (on-screen) hard key area.
// int FLAG_WOKE_HERE            Set if the device woke because of this key event.
  int                           bitsFlags               =
        android.view.KeyEvent.FLAG_SOFT_KEYBOARD            // 2 : the key event was generated by a software keyboard.
      | android.view.KeyEvent.FLAG_KEEP_TOUCH_MODE          // 4 : we don't want the key event to cause us to leave touch mode.
  ;
// int FLAG_EDITOR_ACTION        This mask is used for compatibility, to identify enter keys that are coming from an IME whose enter key has been auto-labelled "next" or "done".


// int META_ALT_ON               Used to check whether one of the ALT meta keys is pressed.
// int META_ALT_LEFT_ON          Used to check whether the left ALT meta key is pressed.
// int META_ALT_RIGHT_ON         Used to check whether the right the ALT meta key is pressed.
// int META_ALT_MASK             Combination of META_ALT_ON, META_ALT_LEFT_ON and META_ALT_RIGHT_ON.
// int META_CTRL_ON              Used to check whether one of the CTRL meta keys is pressed.
// int META_CTRL_LEFT_ON         Used to check whether the left CTRL meta key is pressed.
// int META_CTRL_RIGHT_ON        Used to check whether the right CTRL meta key is pressed.
// int META_CTRL_MASK            Combination of META_CTRL_ON, META_CTRL_LEFT_ON and META_CTRL_RIGHT_ON.
// int META_META_ON              Used to check whether one of the META meta keys is pressed.
// int META_META_LEFT_ON         Used to check whether the left META meta key is pressed.
// int META_META_RIGHT_ON        Used to check whether the right META meta key is pressed.
// int META_META_MASK            Combination of META_META_ON, META_META_LEFT_ON and META_META_RIGHT_ON.
// int META_SHIFT_ON          1: Used to check whether one of the SHIFT meta keys is pressed.
// int META_SHIFT_LEFT_ON        Used to check whether the left SHIFT meta key is pressed.
// int META_SHIFT_RIGHT_ON       Used to check whether the right SHIFT meta key is pressed.
// int META_SHIFT_MASK           Combination of META_SHIFT_ON, META_SHIFT_LEFT_ON and META_SHIFT_RIGHT_ON.
// int META_CAPS_LOCK_ON         Used to check whether the CAPS LOCK meta key is on.
// int META_FUNCTION_ON          Used to check whether the FUNCTION meta key is pressed.
// int META_NUM_LOCK_ON          Used to check whether the NUM LOCK meta key is on.
// int META_SCROLL_LOCK_ON       Used to check whether the SCROLL LOCK meta key is on.
// int META_SYM_ON               Used to check whether the SYM meta key is pressed.
// int MAX_KEYCODE               Deprecated in API level 3. There are now more than MAX_KEYCODE keycodes. Use getMaxKeyCode() instead.
  int                           bitsMeta               = 0;
  if( 1 == a_isKeyShift ){
   bitsMeta = android.view.KeyEvent.META_SHIFT_ON;
   inputconnection.sendKeyEvent(
    new android.view.KeyEvent(
     whenEvent
    ,whenEvent
    ,android.view.KeyEvent.ACTION_DOWN
    ,android.view.KeyEvent.KEYCODE_SHIFT_LEFT
    ,0
    ,bitsMeta
    ,0
    ,0
    ,bitsFlags
    )
   );
  }//if

  inputconnection.sendKeyEvent(
   new android.view.KeyEvent(
    whenEvent                                                        // long downTime   The time (in uptimeMillis()) at which this key code originally went down.
   ,whenEvent                                                        // long eventTime  The time (in uptimeMillis()) at which this event happened.
   ,android.view.KeyEvent.ACTION_DOWN                                // int  action     Action code: either ACTION_DOWN, ACTION_UP, or ACTION_MULTIPLE.
   ,a_isKeyCode                                                      // int  code       The key code.
   ,0                                                                // int  repeat     A repeat count for down events (> 0 if this is after the initial down) or event count for multiple events.
   ,bitsMeta                                                         // int  metaState  Flags indicating which meta keys are currently pressed.
   ,0                                                                // int  deviceId   The device ID that generated the key event.
   ,0                                                                // int  scancode   Raw device scan code of the event.
   ,bitsFlags                                                        // int  flags      The flags for this key event
                                                                     // int  source     The input source such as SOURCE_KEYBOARD.
   )
  );
  inputconnection.sendKeyEvent(
   new android.view.KeyEvent(
    android.os.SystemClock.uptimeMillis()
   ,whenEvent
   ,android.view.KeyEvent.ACTION_UP
   ,a_isKeyCode
   ,0
   ,bitsMeta
   ,0
   ,0
   ,bitsFlags
   )
  );

  if( 1 == a_isKeyShift ){
   inputconnection.sendKeyEvent(
    new android.view.KeyEvent(
     whenEvent
    ,whenEvent
    ,android.view.KeyEvent.ACTION_UP
    ,android.view.KeyEvent.KEYCODE_SHIFT_LEFT
    ,0
    ,bitsMeta
    ,0
    ,0
    ,bitsFlags
    )
   );
  }//if


// This snippet tries to translate the glyph 'primaryCode' into key events:
//  KeyCharacterMap             keycharactermap         = KeyCharacterMap.load( event.getDeviceId() ); // Retrieve the keycharacter map from a keyEvent (build yourself a default event if needed).
//  KeyEvent                    keyevents[]             = null;                                        // Event list to reproduce glyph.
//  char                        ac[]                    = new char[1];
//  ac[0] = primaryCode;                                                  // Put the primary code into an array.
//  keyevents = keycharactermap.getEvents(ac);                            // Retrieve the key events that could have produced this glyph
//  if( null != keyevents ){
//   for( int i = 0; i < keyevents.length; i++ ){ MySendKeyEventHelperMethod( keyevents[i] ); }   // We can reproduce this glyph with this key event array.
//  }else {
//   /* Could not find a way to reproduce this glyph. */
//  }//if
/*///
  if(        0 == a_isKeyShift ){
   inputconnection.sendKeyEvent(
    new android.view.KeyEvent(
     android.view.KeyEvent.ACTION_DOWN
    ,a_isKeyCode
   ));

   inputconnection.sendKeyEvent(
    new android.view.KeyEvent(android.view.KeyEvent.ACTION_UP   ,a_isKeyCode)
   );

  }else if(  1 == a_isKeyShift ){
   inputconnection.sendKeyEvent(
    new android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN ,a_isKeyCode)
   );
   inputconnection.sendKeyEvent(
    new android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN ,a_isKeyCode)
   );
   inputconnection.sendKeyEvent(
    new android.view.KeyEvent(android.view.KeyEvent.ACTION_UP   ,a_isKeyCode)
   );

  }//if
//*///
 }///SendKeySequence//////////////////////////////////////////////////////////////////////////


}///class PanKey//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////

// End of file.
