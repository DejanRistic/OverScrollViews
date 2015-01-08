OverScrollViews
==================

Simple ListView/ScrollView subclasses that allow you to set the over scroll max and hook a listener to see how far you have over scrolled.

To use just grab the OverScrollListView.java or OverScrollView.java file and add it to your project. Its not big enough to throw up on maven central.

OverScrollListView
==================

Define it in your xml, or create from code.

```xml
<com.yourpackage.widgets.OverScrollListView
    android:id="@+id/over_scroll_list"
    android:background="@color/default_light_grey"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

Grab a reference to it in your Activity/Fragment.

```java
@InjectView(R.id.over_scroll_list) // ButterKnife
OverScrollListView listView;
```

Set the height of the overscroll area.

```java
listView.setOverScrollOffsetY(200); // default is 150.  
```
Set a drawable for the overscroll area. Differnce from norm is that we disable the EdgeEffect.
```java
listView.setOverscrollHeader(myDrawable);
```
Attach a listener that gives you the current status of the overscroll. 
The scrollY will always be between 0 and maxY (which is configurable through setOverScrollOffsetY(int n) ) 
```java
listView.setOverScrollListener(new OverScrollListView.OverScrolledListener() {
@Override
public void overScrolled(int scrollY, int maxY, boolean clampedY, boolean didFinishOverScroll) {

    // You can check the scrollY value and use it however you need. (0-maxY)
    if(scrollY < (maxY/2)) { // you are on the lower half of the offset.
        // do something
    }else if(scrollY == maxY) { // you are at full offset.
        // do Something
    }else{  // anything below half offset.
        // do Something
    } 
    
    // Still pulling down after full offset. Value satying at maxY.
    if(clampedY){
        // do something
    }
    
    // You can check if the view is back to 0 offset after its been pulled.
    // This will currently be true if you swipe it back or let it go and let
    // the view bounce.
    if(didFinishOverScroll){
       // do someting
    }
});
```

OverScrollView
==================

The ScrollView works for both top and bottom overscroll events.

Define it in your xml, or create from code.
```xml
<com.yourpackage.widgets.OverScrollView
    android:id="@+id/over_scroll_view"
    android:background="@color/default_light_grey"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <<!--  Your root scrollview group  -->
</com.yourpackage.widgets.OverScrollView
```

Grab a reference to it in your Activity/Fragment.
```java
@InjectView(R.id.over_scroll_view) // ButterKnife
OverScrollView scrollView;
```

Set the height of the overscroll area.
```java
scrollView.setOverScrollOffsetY(200); // default is 150.  
```
The ScrollView is a bit more advanced and currently supports adding a custom view as the overscroll area.
This works for both top and bottom overscroll areas.

```java
TextView textView = new TextView(this);
textView.setText("Hello World");
textView.setTextSize(14);
textView.setGravity(Gravity.CENTER);
textView.setBackgroundColor(Color.YELLOW);

scrollView.setOverscrollHeaderView(textView);
scrollView.setOverscrollFooterView(textView);
```

If you're not into views, you can also use a drawable.
```java
// set a drawable to be shown in the scroll offset area. 
scrollView.setOverscrollHeader(myDrawable);
scrollView.setOverscrollFooter(myDrawable);
```

Same listener as the ListView but the ScrollView listener has events for both top overscroll and bottom overscroll.

The scrollY will always be between 0 and maxY (which is configurable through setOverScrollOffsetY(int n) ) 
```java

// set the overscroll listener. (applies to both OverScrollView and OverScrollListView)
scrollView.setOverScrollListener(new OverScrollView.OverScrolledListener() {
@Override
public void overScrolledTop(int scrollY, int maxY, boolean clampedY, boolean didFinishOverScroll) {
    // handle overscroll (see ListView example for details)
}
@Override
public void overScrolledBottom(int scrollY, int maxY, boolean clampedY, boolean didFinishOverScroll) {
    // handle overscroll (see ListView example for details)
});
```


