#version 150

uniform sampler2D Sampler0;

in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    if(texture2D(Sampler0, texCoord0).r <= 0.5)
        discard;
    fragColor = vec4(vertexColor.rgb, 1);
}