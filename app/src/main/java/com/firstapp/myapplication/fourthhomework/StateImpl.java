package com.firstapp.myapplication.fourthhomework;

/**
 * Состояния и логика их переключения (по вызову next())
 */
public enum StateImpl implements State {

    A {
        @Override
        public State next() {
            return B;
        }
    },

    B {
        @Override
        public State next() {
            return C;
        }
    },

    C {
        @Override
        public State next() {
            return D;
        }
    },

    D {
        @Override
        public State next() {
            return E;
        }
    },

    E {
        @Override
        public State next() {
            return A;
        }
    },

    ; // end of enum

}
