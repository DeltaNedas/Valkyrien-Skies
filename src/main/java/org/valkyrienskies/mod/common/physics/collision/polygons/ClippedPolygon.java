/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2015-2019 the Valkyrien Skies team
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income unless it is to be used as a part of a larger project (IE: "modpacks"), nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from the Valkyrien Skies team.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: The Valkyrien Skies team), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package org.valkyrienskies.mod.common.physics.collision.polygons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.valkyrienskies.mod.common.math.Vector;

public class ClippedPolygon extends Polygon {

    private final List<Vector> clippedVertices;
    private final List<Vector> clippedVerticesUnmodifiable;

    public ClippedPolygon(Polygon other, Vector planeNormal, Vector planePos) {
        super(other);
        this.clippedVertices = new ArrayList<Vector>();
        for (Vector originalVertex : other.getVertices()) {
            if (isVertexInFrontOfCullingPlane(originalVertex, planeNormal, planePos)) {
                // If the vertex is in front of the culling plane, we do not need to cull it.
                clippedVertices.add(originalVertex);
            } else {
                // If its behind the culling plane, we do need to cull it.
                // *Insert Culling Here*
            }
        }
        // Nothing else should be editing this list.
        clippedVerticesUnmodifiable = Collections.unmodifiableList(clippedVertices);
    }

    public List<Vector> getUnmodifiableClippedVertices() {
        return clippedVerticesUnmodifiable;
    }

    private boolean isVertexInFrontOfCullingPlane(Vector vertex, Vector planeNormal,
        Vector planePos) {
        Vector difference = vertex.getSubtraction(planePos);
        // If its less than zero, we are behind the culling plane, so we do not cull this point
        return difference.dot(planeNormal) < 0;
    }
}
