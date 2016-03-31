---
title: Android Theme-安卓样式换肤实践方案
date: 2016-03-30 20:04:03
categories: android 
tags: theme
---
所谓的主题切换，就是能够根据不同的设定，呈现不同风格的界面给用户，也就是所谓的换肤。 本文主要总结了目前作者所知的两种实现方案.

![安卓换肤](http://img.blog.csdn.net/20160331180056383)

<!--more-->


## 概述 

一直以来,认为App换肤是件很酷的东西,不同用户可以自己打造喜欢的颜色,类似墨迹,QQ,或是软件夜间模式,给用户很酷的体验.

在写这个换肤实践方案之前,我也参考了其他人的一些总结,基本上大家都是从这几个思路去实现, 如果出现有误或者不够详细的地方，希望大家提出意见或者自行进行扩展.

基本上换肤实现思路有两类: 1):应用内自定义style. 2):外部加载(apk,压缩资源,插件等) . 本文重要讲内置style及实现的相关步骤, 外部加载只不过是中间加了一部分数据下载和处理,有兴趣的同学自行baidu/google.

## 内置Style

### 1.自定义软件中换肤需要统一处理的属性名,如下 atts.xml

```
	<?xml version="1.0" encoding="utf-8"?>
	<resources>
	    <attr name="mainColor" format="color" />
	    <attr name="mainPrimaryTextColor" format="color" />
	    <attr name="mainPrimaryLightTextColor" format="color" />
	    <attr name="mainBgColor" format="color" />
	</resources>
```
attr里可以定义各种属性类型，如color、float、integer、boolean、dimension（sp、dp/dip、px、pt...）、reference（指向本地资源）等等。

### 2.定义主题

我们需要在资源文件中定义若干套主题。并且在主题中设置各个属性的值。本例子中,重要是针对软件颜色做了不同样式的定义.

```
    <style name="AppTheme.Base.Green">
        <item name="colorPrimary">#4CAF50</item>
        <item name="colorPrimaryDark">#388E3C</item>
        <item name="colorAccent">#9E9E9E</item>
        <item name="mainBgColor">#C8E6C9</item>
        <item name="mainColor">#4CAF50</item>
    </style>

    <style name="AppTheme.Base.Blue">
        <item name="colorPrimary">#2196F3</item>
        <item name="colorPrimaryDark">#1976D2</item>
        <item name="colorAccent">#607D8B</item>
        <item name="mainBgColor">#BBDEFB</item>
        <item name="mainColor">#2196F3</item>
    </style>

    <style name="AppTheme.Base.Purple">
        <item name="colorPrimary">#673AB7</item>
        <item name="colorPrimaryDark">#512DA8</item>
        <item name="colorAccent">#795548</item>
        <item name="mainBgColor">#D1C4E9</item>
        <item name="mainColor">#673AB7</item>
    </style>

    <style name="AppTheme.Base.Grey">
        <item name="colorPrimary">#607D8B</item>
        <item name="colorPrimaryDark">#455A64</item>
        <item name="colorAccent">#FFC107</item>
        <item name="mainBgColor">#CFD8DC</item>
        <item name="mainColor">#607D8B</item>
    </style>
```

### 3.布局文件中使用

资源定义好后,需要订制样式的控件需要引用我们自定义的属性,使用方法如下:

```
	<?xml version="1.0" encoding="utf-8"?>
	<android.support.v7.widget.Toolbar xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    android:id="@+id/toolbar"
	    android:layout_width="match_parent"
	    android:layout_height="wrap_content"
	    android:paddingTop="@dimen/toolbar_padding_top"
	    android:background="?attr/mainColor"
	    android:minHeight="?attr/actionBarSize"
	    app:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
	    app:popupTheme="@style/ThemeOverlay.AppCompat.Light" />
```

这里只列出了tool应用颜色的例子*android:background="?attr/mainColor"* , 当然还可以使用?attr/colorValue、?attr/stringValue与?attr/referenceValue来引用主题中的颜色值、字符串以及图片。

### 4.设置主题及布局文件

布局文件与主题都写好了，接下来我们就要在Activity的onCreate方法里的setContextView前使用了。这里最好写在BaseActivity中,更具share保存的样式值,来动态设置theme

```
    private void setBaseTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                Consts.SHARE_NAME, MODE_PRIVATE);
        int themeType = sharedPreferences.getInt("theme_type", 0);
        int themeId;
        switch (themeType) {
            case THEME_GREEN:
                themeId = R.style.AppTheme_Base_Green;
                break;
            case ThEME_BLUE:
                themeId = R.style.AppTheme_Base_Blue;
                break;
            case THEME_ORANGE:
                themeId = R.style.AppTheme_Base_Orange;
                break;
            case THEME_TEAL:
                themeId = R.style.AppTheme_Base_Teal;
                break;
            case THEME_BROWN:
                themeId = R.style.AppTheme_Base_Brown;
                break;
            case THEME_GREY:
                themeId = R.style.AppTheme_Base_Grey;
                break;
            case THEME_PURPLE:
                themeId = R.style.AppTheme_Base_Purple;
                break;
            default:
                themeId = R.style.AppTheme_Base_Default;
        }
        setTheme(themeId);
    }

```

### 5.样式生效方法

谈到生效方法,大家会有很多说法,也有很多思路, 当然直接设置后立即生效体验会好点, 但是系统限制, 正常的化需要重启activity.

#### 1自定义需要样式变换的所有View, 不需要重启

代表性项目 [MultipleTheme](https://github.com/dersoncheng/MultipleTheme), 为了不重启activity, 自定义了所有需要样式变动的View, 原理为通过对rootView进行遍历，对所有实现了ColorUiInterface的view/viewgroup进行setTheme操作来实现即使刷新的。方式臃肿粗暴, 虽然是不需要重启activity,还可以针对每个view变换过程做动画,但是个人不推荐.

#### 2动态活取每个需要样式变更控件, 活取atts值,对每个控件操作

这种也不用重启activity, 代表性项目 [Colorful](https://github.com/bboyfeiyu/Colorful),虽然对比上一种方式从业务逻辑脱了了一部分,但还是有点繁杂,需要对每个控件做 Setter


```
	ListView  mNewsListView = (ListView) findViewById(R.id.listview);

	// 为ListView设置要修改的属性,在这里没有对ListView本身的属性做修改
	ViewGroupSetter listViewSetter = new ViewGroupSetter(mNewsListView, 0);
	// 绑定ListView的Item View中的news_title视图，在换肤时修改它的text_color属性
	listViewSetter.childViewTextColor(R.id.news_title, R.attr.text_color);


	// 构建Colorful对象
	Colorful mColorful = new Colorful.Builder(this)
		.backgroundDrawable(R.id.root_view, R.attr.root_view_bg) // 设置view的背景图片
		.backgroundColor(R.id.change_btn, R.attr.btn_bg) // 设置按钮的背景色
		.textColor(R.id.textview, R.attr.text_color) // 设置文本颜色
		.setter(listViewSetter)           // 手动设置setter
		.create(); 
```

#### 3乖乖重启activity

既然这个多方法都不能完美去完成项目, 那我们不如按照官方方重启activity,为了美化转变效果,可以正对activity做一些动画,效果也是不错, 如下视频.

<iframe frameborder="0" width="640" height="498" src="http://v.qq.com/iframe/player.html?vid=q0191cq7h61&tiny=0&auto=0" allowfullscreen></iframe>


### 6.代码中如何动态活取自定义属性值

有两种方法如下:


```
        TypedArray a = obtainStyledAttributes(new int[]{R.attr.mainBgColor, 
	R.attr.mainColor});
	int color = a.getColor(0, Color.BLACK)
```

```
	TypedValue typedValue = new TypedValue();
	newTheme.resolveAttribute(mAttrResId, typedValue, true)
	
```

## 聊聊apk方式的主题实现

APK主题方案和主题包保存到SD卡上(墨迹,搜狗实现方式)的方案类似,只不过是apk压缩格式,一些资源的引用可以调用系统api。

APK主题方案的基本思路是：在Android中，所有的资源都是基于包的。资源以id进行标识，在同一个应用中，每个资源都有唯一标识。但在不同的应用中，可以有相同的id。因此，只要获取到了其他应用的Context对象，就可以通过它的getRsources获取到其绑定的资源对象。然后，就可以使用Resources的getXXX方法获取字符串、颜色、dimension、图片等。
要想获取其他应用的Context对象，Android已经为我们提供好了接口。那就是android.content.ContextWrapper.createPackageContext(String packageName, int flags)方法。


```
	try { 
            String remotePackage = "com.your.themepackagename";  
            Context remoteContext = createPackageContext(remotePackage,  
                    CONTEXT_IGNORE_SECURITY);  
            Resources remoteResources = remoteContext.getResources();  
            text.setText(remoteResources.getText(remoteResources.getIdentifier("application_name", "string", remotePackage)));  
            color.setTextColor(remoteResources.getColor(remoteResources.getIdentifier("color_name", "color", remotePackage)));  
            image.setImageDrawable(remoteResources.getDrawable(remoteResources.getIdentifier("ic_icon", "drawable", remotePackage)));  
        } catch (NameNotFoundException e) {  
            e.printStackTrace();  
        }     	
```

***
除了压缩包,apk包等实现方式,还可以考虑插件实现方式,目的都是更好的解耦,更方便的迭代项目.
实践源码[Github](https://github.com/CankingApp/AndroidTheme)

