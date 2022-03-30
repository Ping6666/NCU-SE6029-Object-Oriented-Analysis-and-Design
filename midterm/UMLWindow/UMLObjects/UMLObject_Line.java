package UMLWindow.UMLObjects;

import java.io.File;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import javax.imageio.ImageIO;

/** the base class of umll, typically is basic line */
public class UMLObject_Line {
    protected UMLObject umlo;
    protected int targetSide;

    /** BufferedImage */
    protected BufferedImage bi;

    public UMLObject_Line(UMLObject umlo_, int targetSide_, String fileName) {
        this.umlo = umlo_;
        this.targetSide = targetSide_;
        this.bi = null;
        try {
            this.bi = ImageIO.read(new File(fileName));
        } catch (Exception e) {
            System.out.println("ImageIO.read fail: " + e);
        }
    }

    /** draw the line's arrow part, from coord. p with dir. v */
    protected void drawLineWorkhouse(Graphics g, Point sp, Point ep) {
        if (this.bi == null) {
            return;
        }
        BufferedImage bi_clone = new BufferedImage(this.bi.getWidth(), this.bi.getHeight(), this.bi.getType());
        Graphics2D g2d = bi_clone.createGraphics();
        g2d.drawImage(this.bi, 0, 0, null);
        g2d.dispose();
        Point v = new Point(ep.x - sp.x, ep.y - sp.y);
        // BufferedImage rotate
        double adjust_r = v.x < 0 ? 0 : Math.PI;
        AffineTransform at_ri = AffineTransform.getRotateInstance(
                adjust_r + Math.atan((double) v.y / (double) v.x),
                (1.0 / 2.0) * this.bi.getWidth(null),
                (1.0 / 2.0) * this.bi.getHeight(null));
        AffineTransformOp at_ri_op = new AffineTransformOp(at_ri,
                AffineTransformOp.TYPE_BILINEAR);
        // vector transform with multiplier
        double len = Math.pow(Math.pow(v.x, 2) + Math.pow(v.y, 2), 0.5);
        Point v_ = new Point((int) ((15.0 * v.x) / len), (int) ((15.0 * v.y) / len));
        Point picCoord = new Point(ep.x - v_.x - 15, ep.y - v_.y - 15);
        // draw the line arrow part
        g.drawImage(at_ri_op.filter(bi_clone, null), picCoord.x, picCoord.y, null);
        g.drawLine(sp.x, sp.y, ep.x - (int) (1.8 * v_.x), ep.y - (int) (1.8 * v_.y));
    }
}
