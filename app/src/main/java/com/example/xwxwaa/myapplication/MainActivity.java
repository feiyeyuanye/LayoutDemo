package com.example.xwxwaa.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态创建布局练习
 */
public class MainActivity extends AppCompatActivity {

    // 父布局
    private  LinearLayout linearLayout;
    private  int margin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.ll_layout);
        margin = dp2px(this,10);
        titleLayout();
        topLayout();
        commentLayout();
        btmLayout();
    }

    /**
     * 顶部的RelativeLayout布局
     * title
     */
    private void titleLayout() {
        // title
        RelativeLayout relativeLayout = new RelativeLayout(this);
        // 相当余一个信息包，记录了宽，高，位置等信息。。主要用来告诉父布局，子元素应该位于什么位置。
        // 对于用哪个类，则根据父布局来决定。。是ViewGroup.LayoutParams,还是其子类RelativeLayout.LayoutParams等。
        // 这里的父布局是LinearLayout，所以使用LinearLayout.LayoutParams。
        // 两个参数，是自身的宽高。常用的三个值是；MATCH_PARENT，WRAP_CONTENT，或是固定值（px值）。
        // 这里的高度就是固定值，只是通过方法转换了一下，将dp值转换了px值。所以，只需直接传入dp值就好。
        LinearLayout.LayoutParams rlParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp2px(this,40));
        relativeLayout.setLayoutParams(rlParams);

        // 左侧返回按钮,一般是ImageView,这里用TextView代替。
        TextView tvReturn = new TextView(this);
        RelativeLayout.LayoutParams tvReturnParams = new RelativeLayout.LayoutParams(dp2px(this,40), ViewGroup.LayoutParams.MATCH_PARENT);
        tvReturn.setLayoutParams(tvReturnParams);
        tvReturn.setText("返回");
        tvReturn.setTextColor(getResources().getColor(R.color.black));
        // TextView 其内的内容居中显示。
        tvReturn.setGravity(Gravity.CENTER);
        relativeLayout.addView(tvReturn);

        // 中间的title
        TextView tvTitle = new TextView(this);
        RelativeLayout.LayoutParams tvTitleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tvTitle.setLayoutParams(tvTitleParams);
        tvTitle.setText("商品");
        tvTitle.setTextColor(getResources().getColor(R.color.black));
        tvTitle.setGravity(Gravity.CENTER);
        relativeLayout.addView(tvTitle);

        // 分割线
        View line = new View(this);
        // 这里的父布局是RelativeLayout，所以使用RelativeLayout.LayoutParams。
        RelativeLayout.LayoutParams lineParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(this,1));
        // 当父布局是RelativeLayout时，跟xml编码一样，可以添加相应的规则。
        // 这里是位于父布局的底部。xml中有的规则，这里也都有相应的方法，使用方式一样。
        // addRule两个参数，第一个参数是规则，第二个参数是相对元素的id值。
        lineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        line.setLayoutParams(lineParams);
        line.setBackgroundColor(getResources().getColor(R.color.red));
        relativeLayout.addView(line);

        // 将顶部的title布局添加到父布局中
        linearLayout.addView(relativeLayout);

    }

    /**
     * 商品展示的布局，是个FrameLayout
     * 底是张ImageView图片，右下角是个计数的TextView
     */
    private void topLayout() {
        // 商品展示
        FrameLayout fl = new FrameLayout(this);
        LinearLayout.LayoutParams flParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp2px(this,250));
        fl.setLayoutParams(flParams);

        // 商品图片
        ImageView iv = new ImageView(this);
        FrameLayout.LayoutParams ivParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(ivParams);
        iv.setImageResource(R.mipmap.libai);
        fl.addView(iv);

        // 右下角计数的TextView
        TextView tvIndex = new TextView(this);
        tvIndex.setText("1/5");
        tvIndex.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams tvIndexParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvIndexParams.setMargins(0,0,dp2px(this,10),dp2px(this,10));
        // 帧布局的特性，会显示在图片之上。并且会位于右下角。
        tvIndexParams.gravity=Gravity.RIGHT|Gravity.BOTTOM;
        tvIndex.setLayoutParams(tvIndexParams);
        fl.addView(tvIndex);

        linearLayout.addView(fl);

        View lines = new View(this);
        lines.setBackgroundColor(getResources().getColor(R.color.red));
        LinearLayout.LayoutParams linesParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp2px(this,1));
        lines.setLayoutParams(linesParams);
        linearLayout.addView(lines);
    }

    /**
     * 评论区域，是一个RelativeLayout
     * 分点赞和评论。
     * 点赞里，会动态创建点赞头像，并自动换行。
     * 这里的实现方法并不好，是用垂直的LinearLayout嵌套多层横向的LinearLayout来做的；
     * 1，垂直的LinearlLayout作为父布局
     * 2，然后得到父布局的宽度，并根据头像的宽度，计算出一行可以放置几个头像
     * 3，多余的头像会另起一行放置。并且每行都会新创建横向的LinearLayout来安置。
     * 也可以使用TableLayout来实现。
     */
    private void commentLayout() {
        // 三角形
        View triangle = new View(this);
        LinearLayout.LayoutParams triangleParams = new LinearLayout.LayoutParams(dp2px(this,15),dp2px(this,15));
        triangleParams.setMargins(dp2px(this,35),dp2px(this,5),0,0);
        triangle.setLayoutParams(triangleParams);
        triangle.setBackgroundResource(R.drawable.shape_triangle);
        linearLayout.addView(triangle);

        // 评论区域
        RelativeLayout commentLayout = new RelativeLayout(this);
        commentLayout.setBackgroundColor(getResources().getColor(R.color.bg_comment));
        LinearLayout.LayoutParams commentLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        commentLayoutParams.setMargins(dp2px(this,20),0,dp2px(this,20),dp2px(this,20));
        commentLayout.setLayoutParams(commentLayoutParams);
        commentLayout.setPadding(0,margin,0,margin);
        linearLayout.addView(commentLayout);

        // 点赞标识，一般是个ImageView
        TextView tvLove= new TextView(this);
        RelativeLayout.LayoutParams loveParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loveParams.setMargins(margin,0,margin,0);
        tvLove.setLayoutParams(loveParams);
        tvLove.setText("点赞");
        tvLove.setTextColor(getResources().getColor(R.color.black));
        tvLove.setId(R.id.tv_love);
        commentLayout.addView(tvLove);

        // 点赞列表.
        final LinearLayout llLike = new LinearLayout(this);
        RelativeLayout.LayoutParams likeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        likeParams.addRule(RelativeLayout.RIGHT_OF,tvLove.getId());
        likeParams.addRule(RelativeLayout.ALIGN_TOP,tvLove.getId());
        llLike.setLayoutParams(likeParams);
        llLike.setOrientation(LinearLayout.VERTICAL);
        commentLayout.addView(llLike);
        llLike.setId(R.id.like_layout);
        // 因为在onCreate()方法中，是无法直接拿到元素宽高的。
        // 实际上，都是通过调用接口之后，再来做评论区的显示，所以实际并不需要这个方法。
        llLike.post(new Runnable() {
            @Override
            public void run() {
                // 注意每次清除子View。因为一般都会有刷新，避免每次刷新的重复添加。
                if (llLike.getChildCount() > 0){
                    llLike.removeAllViews();
                }
                // 点赞头像的父布局宽度
                int viewLength = llLike.getMeasuredWidth();
                // 计算出一行能放下几个头像。。。32+5的意思，是头像宽度加上边距。
                int index = viewLength / dp2px(MainActivity.this, 32 + 5);
                int row;
                // 通过点赞集合长度，计算出有几行。
                // 正常会从服务器获取到有多少点赞，这里模拟一下，mList为点赞数据的列表。
                List<String> mList = new ArrayList<>();
                for (int i=0;i<10;i++){
                    mList.add("");
                }
                int listSize = mList.size();
                // 根据余数来判断；
                // 如果余数为0，说明正好整行。如果不为0，说明有多余的，则再多添加一行。
                // 这个row就是最后有几行，而index则是每行有几个。
                if (listSize % index == 0) {
                    row = listSize / index;
                } else {
                    row = listSize / index + 1;
                }
                int count = 0;
                for (int i = 0; i < row; i++) {
                    // 第一层循环为行，先创建横向的LinearLayout作为父布局，来安置头像。
                    LinearLayout likeLayout = new LinearLayout(MainActivity.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 0, dp2px(MainActivity.this, 10));
                    likeLayout.setLayoutParams(params);
                    likeLayout.setOrientation(LinearLayout.HORIZONTAL);
                    for (int j = 0; j < index; j++) {
                        // 第二层循环是列，每行有几列。
                        ImageView circularImage = new ImageView(MainActivity.this);
                        LinearLayout.LayoutParams cirParams = new LinearLayout.LayoutParams(dp2px(MainActivity.this, 32), dp2px(MainActivity.this, 32));
                        cirParams.setMargins(0, 0, dp2px(MainActivity.this, 5), 0);
                        circularImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                        circularImage.setLayoutParams(cirParams);
                        circularImage.setImageResource(R.mipmap.ic_launcher_round);
                        likeLayout.addView(circularImage);
                        // 这里的count用来计数，每次循环都加1。
                        // 当count的值等于listSize-1的值，说明已经显示了全部数据，则跳出循环。
                        if (count == listSize - 1) {
                            break;
                        } else {
                            count++;
                        }
                    }
                    llLike.addView(likeLayout);
                }
            }
        });

        // 分割线，红色醒目
        View spaceLines = new View(this);
        RelativeLayout.LayoutParams spaceLinesParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp2px(this,1));
        spaceLinesParams.addRule(RelativeLayout.BELOW,llLike.getId());
        spaceLines.setLayoutParams(spaceLinesParams);
        spaceLines.setBackgroundColor(getResources().getColor(R.color.red));
        spaceLines.setId(R.id.space_lines);
        commentLayout.addView(spaceLines);

        // 评论标识，，一般是个ImageView
        TextView tvComment= new TextView(this);
        RelativeLayout.LayoutParams commentParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        commentParams.addRule(RelativeLayout.BELOW,spaceLines.getId());
        commentParams.addRule(RelativeLayout.ALIGN_LEFT,tvLove.getId());
        commentParams.setMargins(0,margin,margin,0);
        tvComment.setLayoutParams(commentParams);
        tvComment.setText("评论");
        tvComment.setTextColor(getResources().getColor(R.color.black));
        tvComment.setId(R.id.tv_comment);
        commentLayout.addView(tvComment);
        // 评论内容
        TextView textView = new TextView(this);
        RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewParams.addRule(RelativeLayout.RIGHT_OF,tvComment.getId());
        textViewParams.addRule(RelativeLayout.ALIGN_TOP,tvComment.getId());
        textView.setLayoutParams(textViewParams);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setText("一篇诗，一斗酒，\n一曲长歌，一剑天涯。");
        commentLayout.addView(textView);
    }

    /**
     * 底部布局，是一个LinearLayout
     * 左侧是价格，使用了SpannableStringBuilder
     * 右侧是购买按钮，使用了GradientDrawable
     */
    private void btmLayout() {
        // 这里使用了Space标签，目的是为了把底部的LinearLayout顶下去。
        // 因为在垂直的线性布局中，控制子元素的上下位置的属性是失效的。
        // 其实，在Linearlayout中使用weight属性，是会对它的绘制效率产生影响的。
        Space space = new Space(this);
        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        spaceParams.weight = 1;
        space.setLayoutParams(spaceParams);
        linearLayout.addView(space);

        // 底部布局
        LinearLayout btmLayout = new LinearLayout(this);
        LinearLayout.LayoutParams btmParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp2px(this,40));
        btmLayout.setLayoutParams(btmParams);
        btmLayout.setBackgroundColor(getResources().getColor(R.color.btm_layout));
        btmLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(btmLayout);

        // 左侧价格显示
        TextView price = new TextView(this);
        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        // LinearLayout的特性，权重。
        priceParams.weight = 3;
        price.setLayoutParams(priceParams);
        price.setGravity(Gravity.CENTER_VERTICAL);
        price.setPadding(margin,0,0,0);
        // SpannableStringBuilder,用来动态修改TextView的内容。
        String str="￥";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        // 字体颜色，SPAN_INCLUSIVE_INCLUSIVE 前面包括，后面包括。意思就是，￥和68都会应用这个颜色。
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF0000"));
        spannableStringBuilder.setSpan(colorSpan, 0, str.length(), Spannable. SPAN_INCLUSIVE_INCLUSIVE);
        // SpannableStringBuilder要追加的内容。也可不追加，使用一个SpannableString直接通过下角标来操作。
        String strAppand = "68";
        SpannableString spannableString = new SpannableString(strAppand);
        // 字体大小，SPAN_EXCLUSIVE_INCLUSIVE 前面不包括，后面包括。意思就是，￥不会应用这个字体大小，68会应用。
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(20,true);
        spannableString.setSpan(absoluteSizeSpan, 0, strAppand.length(), Spannable. SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder.append(spannableString);
        price.setText(spannableStringBuilder);
        btmLayout.addView(price);

        // 右侧购买按钮
        TextView buy = new TextView(this);
        LinearLayout.LayoutParams buyParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        // LinearLayout的特性，权重。
        buyParams.weight = 1 ;
        buy.setLayoutParams(buyParams);
        buy.setGravity(Gravity.CENTER);
        buy.setText("购买");
        buy.setTextColor(getResources().getColor(R.color.white));
        btmLayout.addView(buy);
        buy.setBackgroundResource(R.drawable.shape_use);
        // 动态设置shape的值。shape_use是一个共用的xml。
        // 注意，所有使用shape_use的位置，都要设置下面的属性来覆盖，否则会继承这些值。
        // 设置的三个属性，依次是边框，填充色，和圆角。
        // 边框为透明色，且值是0。填充色是红色。没有圆角。
        GradientDrawable layoutDrawable = (GradientDrawable) buy.getBackground();
        layoutDrawable.setStroke(0,getResources().getColor(R.color.transparency));
        layoutDrawable.setColor(getResources().getColor(R.color.red));
        layoutDrawable.setCornerRadius(0);
    }

    //dp转px；
    public static int dp2px(Context mContext,int dp){
        return (int) (dp * mContext.getResources().getDisplayMetrics().density + 0.5);
    }
}
