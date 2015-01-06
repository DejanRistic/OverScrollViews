OverScrollViews
==================

Simple ListView/ScrollView subclasses that allow you to set the over scroll max and hook a listener to see how far you have over scrolled.

To use just grab the OverScrollListView.java or OverScrollView.java file and add it to your project. Its not big enough to throw up on maven central.

You can add either of the views to your xml file or set it up in code like any other widget.

```xml
<com.yourpackage.widgets.OverScrollListView
    android:id="@+id/over_scroll_list"
    android:background="@color/default_light_grey"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
    
<com.yourpackage.widgets.OverScrollView
    android:id="@+id/over_scroll_view"
    android:background="@color/default_light_grey"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <<!--  Your root scrollview group  -->
</com.yourpackage.widgets.OverScrollView
```

Then grab a reference to it in your Activity/Fragment.

```java
@InjectView(R.id.over_scroll_list) // ButterKnife
OverScrollListView listView;

@InjectView(R.id.over_scroll_view) // ButterKnife
OverScrollView scrollView;
```

Initialize like any other list/scroll view, the following are examples of what they can do.

```java
listView.setOverScrollOffsetY(200); // default is 150.  
scrollView.setOverScrollOffsetY(200); // default is 150.  
```

The ScrollView currently supports adding a custom view as the overscroll area. 
```java

// ScrollView only, allows you to set a custom view for the over scroll area.
TextView textView = new TextView(this);
textView.setText("Hello World");
textView.setTextSize(14);
textView.setGravity(Gravity.CENTER);
textView.setBackgroundColor(Color.YELLOW);

scrollView.setOverscrollView(textView);
```

Both support the ability to add a drawable for the over scroll area.

```java
// set a drawable to be shown in the scroll offset area. 
listView.setOverscrollHeader(myDrawable);
scrollView.setOverscrollHeader(myDrawable);
```

Both support an OverScrolledListener that will notify you of the scroll offset and such.
```java

// set the overscroll listener. (applies to both OverScrollView and OverScrollListView)
listView.setOverScrollListener(new OverScrollListView.OverScrolledListener() {
@Override
public void overScrolled(int scrollY, int maxY, boolean clampedY, boolean didFinishOverScroll) {

    // You can check the scrollY value and use it however you need. (0-maxY)
    if(scrollY < (maxY/2)) { // you are half way to full offset.
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

Listener is the same for the ScrollView, we just apply the OverScrollView.OverScrolledListener() instead.

```java
scrollView.setOverScrollListener(new OverScrollView.OverScrolledListener() {
...
}
```


