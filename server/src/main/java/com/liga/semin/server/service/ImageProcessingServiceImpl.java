package com.liga.semin.server.service;

import com.liga.semin.server.exception.ImageProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageProcessingServiceImpl implements ImageProcessingService {
    private static final Logger logger = LoggerFactory.getLogger(ImageProcessingServiceImpl.class);

    @Override
    public byte[] putProfileOnImage(String text) {
        logger.debug("Processing user's profile");

        final int body_size_font;
        if (text.length() < 125) {
            body_size_font = 48;
        } else if (text.length() < 200) {
            body_size_font = 42;
        } else {
            body_size_font = 38;
        }
        String header;
        int firstEndOfString = text.indexOf("\n");
        if (firstEndOfString == -1) { // если строка одна, то заголовок - первое слово
            int firstSpace = text.indexOf(" ");
            header = text.substring(0, firstSpace == -1 ? text.length() : firstSpace); // если в описании одно слово - оно заголовок
            if (text.equals(header)) {
                text = " ";
            } else  {
                text = text.substring(firstSpace + 1); // min - если вдруг в описании одно слово
            }
        } else { // иначе заголовок - первая строка
            header = text.substring(0, firstEndOfString);
            text = text.substring(firstEndOfString + 1);
        }
        text = text.replaceAll("\n", " ");
        try (var imageStream = ImageProcessingServiceImpl.class.getClassLoader().getResourceAsStream("prerev-background.jpg")) {
            BufferedImage image = ImageIO.read(imageStream);
            final int IMG_WIDTH = image.getWidth();
            final int IMG_HEIGHT = image.getHeight();
            Graphics graphics = image.getGraphics();
            var g2d = (Graphics2D) graphics;
            FontRenderContext fontRenderContext = g2d.getFontRenderContext();


            Font fontHeader = new Font("Old Standard TT", Font.BOLD, (int) (body_size_font * 1.2));
            Font fontBody = new Font("Old Standard TT", Font.PLAIN, body_size_font);
            FontMetrics headerMetric = graphics.getFontMetrics(fontHeader);
            FontMetrics bodyMetric = graphics.getFontMetrics(fontBody);

            var linesHeader = splitByLines(header, fontHeader, IMG_WIDTH, fontRenderContext);
            float y = drawLinesHeader(linesHeader, IMG_WIDTH, g2d, headerMetric);

            var linesBody = splitByLines(text, fontBody, IMG_WIDTH, fontRenderContext);
            drawLinesBody(linesBody, g2d, y+10, bodyMetric);
            g2d.dispose();

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "png", os);
            return os.toByteArray();
        } catch (Exception e) {
            throw new ImageProcessingException(e.getMessage());
        }
    }

    private static void drawLinesBody(List<TextLayout> linesBody, Graphics2D g2d, float y, FontMetrics bodyMetric) {
        logger.debug("Drawing user's profile body lines");

        for (TextLayout line : linesBody) {
            line.draw(g2d, 15, y + line.getAscent());
            y += bodyMetric.getHeight();
        }
    }

    private static float drawLinesHeader(List<TextLayout> linesHeader, int IMG_WIDTH, Graphics2D g2d, FontMetrics headerMetric) {
        logger.debug("Drawing user's profile header lines");

        float y = 10;
        for (TextLayout line : linesHeader) {
            Rectangle2D bounds = line.getBounds();
            float x = (IMG_WIDTH - (float) bounds.getWidth()) / 2; // центрирование
            line.draw(g2d, x, y + line.getAscent());
            y += headerMetric.getHeight();
        }
        return y;
    }

    private static List<TextLayout> splitByLines(String str, Font font, final int IMG_WIDTH, FontRenderContext fontRenderContext) {
        logger.debug("Splitting user's profile by lines");

        AttributedString attributedHeader = new AttributedString(str);
        attributedHeader.addAttribute(TextAttribute.FONT, font);
        attributedHeader.addAttribute(TextAttribute.FOREGROUND, Color.BLACK);

        LineBreakMeasurer headerMeasurer = new LineBreakMeasurer(attributedHeader.getIterator(), fontRenderContext);
        java.util.List<TextLayout> linesHeader = new ArrayList<>();
        while (headerMeasurer.getPosition() < str.length()) {
            linesHeader.add(headerMeasurer.nextLayout(IMG_WIDTH * 0.96f));
        }
        return linesHeader;
    }
}
