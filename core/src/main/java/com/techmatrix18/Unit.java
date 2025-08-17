package com.techmatrix18;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Разработано для techmatrix18.com
 *
 * @autor: Alexander Kuziv
 * @date: 17-08-2025
 * @version: 0.0.1
 */

class Unit {
    float x, y;
    TextureRegion up, down, left, right;
    TextureRegion upSel, downSel, leftSel, rightSel;
    TextureRegion current;
    boolean selected = false;
    Float targetX = null, targetY = null;

    Unit(float x, float y,
         TextureRegion up, TextureRegion down, TextureRegion left, TextureRegion right,
         TextureRegion upSel, TextureRegion downSel, TextureRegion leftSel, TextureRegion rightSel) {
        this.x = x;
        this.y = y;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.upSel = upSel;
        this.downSel = downSel;
        this.leftSel = leftSel;
        this.rightSel = rightSel;
        this.current = down;
    }

    // 🔹 Добавляем этот метод
    public void updateCurrent() {
        if (selected) {
            if (current == up) current = upSel;
            else if (current == down) current = downSel;
            else if (current == left) current = leftSel;
            else if (current == right) current = rightSel;
        } else {
            if (current == upSel) current = up;
            else if (current == downSel) current = down;
            else if (current == leftSel) current = left;
            else if (current == rightSel) current = right;
        }
    }
}

