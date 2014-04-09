/*
    AutomateAndroid
    Copyright (C) 2014 JIANG Huai

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/


package individual.aurthconan.automateandroid.module;

import java.util.Vector;

public class ClassDefinition {

    public enum TYPE{
        UNSUPPORTED_TYPE,
        STRING_TYPE,
        INTEGER_TYPE,
        BOOLEAN_TYPE,
        DOUBLE_TYPE,
        VOID_TYPE,
    }

    static public class MethodDefinition {
        public TYPE mReturnType;
        public String mName;
        public Vector<TYPE> mArgsType = new Vector<TYPE>();
    }

    public String mName;
    public Vector<MethodDefinition> mMethods = new Vector<MethodDefinition>();
}
