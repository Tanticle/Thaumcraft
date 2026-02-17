package art.arcane.thaumcraft.client.rendering.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WavefrontObject {

    private final List<float[]> vertices = new ArrayList<>();
    private final List<float[]> normals = new ArrayList<>();
    private final List<float[]> texCoords = new ArrayList<>();
    private final List<GroupObject> groupObjects = new ArrayList<>();
    private GroupObject currentGroup;
    private final String fileName;

    public WavefrontObject(ResourceLocation resource) {
        this.fileName = resource.toString();
        try {
            Resource res = Minecraft.getInstance().getResourceManager().getResourceOrThrow(resource);
            loadObjModel(res.open());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load OBJ model: " + fileName, e);
        }
    }

    public WavefrontObject(String filename, InputStream inputStream) {
        this.fileName = filename;
        loadObjModel(inputStream);
    }

    private void loadObjModel(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("\\s+", " ").trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                if (line.startsWith("v ")) {
                    parseVertex(line);
                } else if (line.startsWith("vn ")) {
                    parseNormal(line);
                } else if (line.startsWith("vt ")) {
                    parseTexCoord(line);
                } else if (line.startsWith("f ")) {
                    if (currentGroup == null) currentGroup = new GroupObject("Default");
                    parseFace(line);
                } else if (line.startsWith("g ") || line.startsWith("o ")) {
                    String name = line.substring(line.indexOf(" ") + 1).trim();
                    if (!name.isEmpty()) {
                        if (currentGroup != null) groupObjects.add(currentGroup);
                        currentGroup = new GroupObject(name);
                    }
                }
            }
            if (currentGroup != null) groupObjects.add(currentGroup);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse OBJ model: " + fileName, e);
        }
    }

    private void parseVertex(String line) {
        String[] tokens = line.substring(2).trim().split(" ");
        vertices.add(new float[]{Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])});
    }

    private void parseNormal(String line) {
        String[] tokens = line.substring(3).trim().split(" ");
        normals.add(new float[]{Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])});
    }

    private void parseTexCoord(String line) {
        String[] tokens = line.substring(3).trim().split(" ");
        texCoords.add(new float[]{Float.parseFloat(tokens[0]), 1.0f - Float.parseFloat(tokens[1])});
    }

    private void parseFace(String line) {
        String[] tokens = line.substring(2).trim().split(" ");
        int[][] faceData = new int[tokens.length][3];

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (token.contains("//")) {
                String[] parts = token.split("//");
                faceData[i][0] = Integer.parseInt(parts[0]) - 1;
                faceData[i][1] = -1;
                faceData[i][2] = Integer.parseInt(parts[1]) - 1;
            } else if (token.contains("/")) {
                String[] parts = token.split("/");
                faceData[i][0] = Integer.parseInt(parts[0]) - 1;
                faceData[i][1] = Integer.parseInt(parts[1]) - 1;
                faceData[i][2] = parts.length > 2 ? Integer.parseInt(parts[2]) - 1 : -1;
            } else {
                faceData[i][0] = Integer.parseInt(token) - 1;
                faceData[i][1] = -1;
                faceData[i][2] = -1;
            }
        }

        if (tokens.length == 3 || tokens.length == 4) {
            currentGroup.faces.add(faceData);
        } else if (tokens.length > 4) {
            for (int i = 1; i < tokens.length - 1; i++) {
                currentGroup.faces.add(new int[][]{faceData[0], faceData[i], faceData[i + 1]});
            }
        }
    }

    public void renderAll(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, int color) {
        renderAll(poseStack, consumer, packedLight, packedOverlay, color, false);
    }

    public void renderAll(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, int color, boolean flipV) {
        for (GroupObject group : groupObjects) {
            renderGroup(group, poseStack, consumer, packedLight, packedOverlay, color, flipV);
        }
    }

    public void renderPart(String partName, PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, int color) {
        renderPart(partName, poseStack, consumer, packedLight, packedOverlay, color, false);
    }

    public void renderPart(String partName, PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, int color, boolean flipV) {
        for (GroupObject group : groupObjects) {
            if (partName.equalsIgnoreCase(group.name)) {
                renderGroup(group, poseStack, consumer, packedLight, packedOverlay, color, flipV);
            }
        }
    }

    public void renderOnly(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, int color, String... groupNames) {
        renderOnly(poseStack, consumer, packedLight, packedOverlay, color, false, groupNames);
    }

    public void renderOnly(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, int color, boolean flipV, String... groupNames) {
        for (GroupObject group : groupObjects) {
            for (String name : groupNames) {
                if (name.equalsIgnoreCase(group.name)) {
                    renderGroup(group, poseStack, consumer, packedLight, packedOverlay, color, flipV);
                }
            }
        }
    }

    public void renderAllExcept(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, int color, String... excludeNames) {
        renderAllExcept(poseStack, consumer, packedLight, packedOverlay, color, false, excludeNames);
    }

    public void renderAllExcept(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, int color, boolean flipV, String... excludeNames) {
        for (GroupObject group : groupObjects) {
            boolean skip = false;
            for (String name : excludeNames) {
                if (name.equalsIgnoreCase(group.name)) {
                    skip = true;
                    break;
                }
            }
            if (!skip) {
                renderGroup(group, poseStack, consumer, packedLight, packedOverlay, color, flipV);
            }
        }
    }

    public String[] getPartNames() {
        return groupObjects.stream().map(g -> g.name).toArray(String[]::new);
    }

    private void renderGroup(GroupObject group, PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, int color, boolean flipV) {
        PoseStack.Pose pose = poseStack.last();

        for (int[][] face : group.faces) {
            Vector3f faceNormal = null;
            for (int i = 0; i < 4; i++) {
                int[] vertexData = face[Math.min(i, face.length - 1)];
                float[] v = vertices.get(vertexData[0]);

                float u = 0, vt = 0;
                if (vertexData[1] >= 0 && vertexData[1] < texCoords.size()) {
                    float[] tc = texCoords.get(vertexData[1]);
                    u = tc[0];
                    vt = flipV ? 1.0f - tc[1] : tc[1];
                }

                float nx = 0, ny = 1, nz = 0;
                if (vertexData[2] >= 0 && vertexData[2] < normals.size()) {
                    float[] n = normals.get(vertexData[2]);
                    nx = n[0];
                    ny = n[1];
                    nz = n[2];
                } else {
                    if (faceNormal == null) faceNormal = calculateFaceNormal(face);
                    nx = faceNormal.x();
                    ny = faceNormal.y();
                    nz = faceNormal.z();
                }

                consumer.addVertex(pose, v[0], v[1], v[2])
                        .setColor(color)
                        .setUv(u, vt)
                        .setOverlay(packedOverlay)
                        .setLight(packedLight)
                        .setNormal(pose, nx, ny, nz);
            }
        }
    }

    private Vector3f calculateFaceNormal(int[][] face) {
        if (face.length < 3) return new Vector3f(0, 1, 0);
        float[] v0 = vertices.get(face[0][0]);
        float[] v1 = vertices.get(face[1][0]);
        float[] v2 = vertices.get(face[2][0]);

        Vector3f edge1 = new Vector3f(v1[0] - v0[0], v1[1] - v0[1], v1[2] - v0[2]);
        Vector3f edge2 = new Vector3f(v2[0] - v0[0], v2[1] - v0[1], v2[2] - v0[2]);
        return edge1.cross(edge2).normalize();
    }

    private static class GroupObject {
        final String name;
        final List<int[][]> faces = new ArrayList<>();

        GroupObject(String name) {
            this.name = name;
        }
    }
}
