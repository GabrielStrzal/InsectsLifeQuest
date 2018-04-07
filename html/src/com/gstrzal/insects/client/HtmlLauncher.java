package com.gstrzal.insects.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.GameConfig;
import com.gstrzal.insects.resolvers.ActionResolverDummy;

// Based on Benjamin Schulte (MrStahlfelge) and Dennis (kwlum) solutions for canvas resizing.
// https://github.com/MrStahlfelge
// https://github.com/kwlum

public class HtmlLauncher extends GwtApplication {

    private static final int PADDING = 0;
    private int WIDTH = (int) GameConfig.DISPLAY_SCREEN_WIDTH_PX;
    private int HEIGHT = (int)GameConfig.DISPLAY_SCREEN_HEIGHT_PX;
    private GwtApplicationConfiguration cfg;

    @Override
    public GwtApplicationConfiguration getConfig() {

        cfg = new GwtApplicationConfiguration(WIDTH, HEIGHT);
        Window.enableScrolling(false);
        Window.setMargin("0");
        Window.addResizeHandler(new ResizeListener());
        cfg.preferFlash = false;
        return cfg;
    }

    public ApplicationListener createApplicationListener () {
        return new Insects(new ActionResolverDummy());
    }

    class ResizeListener implements ResizeHandler {
             @Override
             public void onResize(ResizeEvent event) {
                 int width = event.getWidth() - PADDING;
                 int height = event.getHeight() - PADDING;
                 getRootPanel().setWidth("" + width + "px");
                 getRootPanel().setHeight("" + height + "px");
                 getApplicationListener().resize(width, height);
                 Gdx.graphics.setWindowedMode(width, height);


                 int innerWidth = Window.getClientWidth();
                 int innerHeight = Window.getClientHeight();
                 int newWidth = innerWidth;
                 int newHeight = innerHeight;
                 float ratio = innerWidth / (float) innerHeight;
                 float viewRatio = WIDTH / (float) HEIGHT;

                 if (ratio > viewRatio) {
                     newWidth = (int) (innerHeight * viewRatio);
                 } else {
                     newHeight = (int) (innerWidth / viewRatio);
                 }

                 Element element = Document.get().getElementById("embed-html");
                 NodeList<Element> nl = element.getElementsByTagName("canvas");

                 if (nl != null && nl.getLength() > 0) {
                     Element canvas = nl.getItem(0);
                     canvas.setAttribute("width", "" + newWidth + "px");
                     canvas.setAttribute("height", "" + newHeight + "px");
                     canvas.getStyle().setWidth(newWidth, Style.Unit.PX);
                     canvas.getStyle().setHeight(newHeight, Style.Unit.PX);
                     canvas.getStyle().setTop((int) ((innerHeight - newHeight) * 0.5f), Style.Unit.PX);
                     canvas.getStyle().setLeft((int) ((innerWidth - newWidth) * 0.5f), Style.Unit.PX);
                     canvas.getStyle().setPosition(Style.Position.ABSOLUTE);
                 }
             }
    }


}