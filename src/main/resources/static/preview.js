// display preveiw
function initImage() {
    const urlParams = new URLSearchParams(window.location.search);
    const fileName = urlParams.get("filename");
    document.getElementsByTagName("img")[0].src = "/preview/" + fileName;
}
function initLayout() {
    const urlParams = new URLSearchParams(window.location.search);
    const layout = urlParams.get("layout").split("_"); // layout = [X,Y,WIDTH,HEIGHT]
    document.querySelectorAll(".line.top")[0].style = "top: " + eval(layout[1] + "-1") + "px";
    document.querySelectorAll(".line.right")[0].style = "left: " + eval(layout[0] + "+" + layout[2] + "+1") + "px";
    document.querySelectorAll(".line.bottom")[0].style = "top: " + eval(layout[1] + "+" + layout[3] + "+1") + "px";
    document.querySelectorAll(".line.left")[0].style = "left: " + eval(layout[0] + "-1") + "px";
    document.querySelectorAll(".label.x")[0].style = "left: " + layout[0] + "px; top: 0";
    document.querySelectorAll(".label.y")[0].style = "top: " + layout[1] + "px; left: 0";
    document.querySelectorAll(".label.width")[0].style = "left: " + eval(layout[0] + "+" + layout[2] + "/2-51") + "px; top: " + eval(layout[1] + "+" + layout[3] + "-29");
    document.querySelectorAll(".label.height")[0].style = "top: " + eval(layout[1] + "+" + layout[3] + "/2-19") + "px; left: " + eval(layout[0] + "+" + layout[2] + "-102");
    document.querySelectorAll(".label.x span")[0].innerHTML = layout[0];
    document.querySelectorAll(".label.y span")[0].innerHTML = layout[1];
    document.querySelectorAll(".label.width span")[0].innerHTML = layout[2];
    document.querySelectorAll(".label.height span")[0].innerHTML = layout[3];
}
initImage();
initLayout();