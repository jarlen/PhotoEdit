//
// Created by hjl on 2016/9/20.
//

#ifndef PHOTOEDIT_COLOR_H
#define PHOTOEDIT_COLOR_H

int alpha(int color) {
    return (color & 0xFF000000) >> 24;
}

int red(int color) {
    return (color & 0xFF0000) >> 16;
}

int green(int color) {
    return (color & 0xFF00) >> 8;
}

int blue(int color) {
    return color & 0xFF;
}

int ARGB(int alpha, int red, int green, int blue) {
    return (alpha << 24) | (red << 16) | (green << 8) | blue;
}

int RGB(int red, int green, int blue) {
    return 255 << 24 | red << 16 | green << 8 | blue;
}

float Max(float x, float y) {
    return x > y ? x : y;
}

float Min(float x, float y) {
    return x > y ? y : x;
}

#endif //PHOTOEDIT_COLOR_H
