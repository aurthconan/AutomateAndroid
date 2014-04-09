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

import individual.aurthconan.automateandroid.module.ClassDefinition.MethodDefinition;
import individual.aurthconan.automateandroid.module.ClassDefinition.TYPE;

import java.util.Vector;

import org.mozilla.javascript.ScriptableObject;

public class ModuleManager {
    static public Vector<ScriptableObject> getModule() {
        ClassDefinition def = new ClassDefinition();
        def.mName = "test";
        MethodDefinition method = new MethodDefinition();
        method.mName = "TestMethod";
        method.mReturnType = ClassDefinition.TYPE.VOID_TYPE;
        method.mArgsType.add(TYPE.BOOLEAN_TYPE);
        def.mMethods.add(method);

        Vector<ScriptableObject> vector = new Vector<ScriptableObject>();
        vector.add(new ModuleBridge(def));
        return vector;
    }
}
