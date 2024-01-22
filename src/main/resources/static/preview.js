// display preveiw
function initImage() {
    const urlParams = new URLSearchParams(window.location.search);
    const fileName = urlParams.get("filename");
    document.getElementsByTagName("img")[0].src = "/preview/" + fileName;
}
function initLayout() {
    const urlParams = new URLSearchParams(window.location.search);
    const layout = urlParams.get("layout").split("_"); // layout = [X,Y,WIDTH,HEIGHT]
    red_top.style.top = eval(layout[1] + "-1") + "px";
    red_right.style.left = eval(layout[2] + "-1") + "px";
    red_bottom.style.top = eval(layout[3] + "-1") + "px";
    red_left.style.left = eval(layout[0] + "-1") + "px";
    x1.style.left = layout[0] + "px";
    y1.style.top = layout[1] + "px";
    x2.style.left = layout[2] + "px";
    y2.style.top = layout[3] + "px";
    x1.innerHTML = "X1=" + layout[0];
    y1.innerHTML = "Y1=" + layout[1];
    x2.innerHTML = "X2=" + layout[2];
    y2.innerHTML = "Y2=" + layout[3];
}
function hove(e) {
    const pageX = e.pageX;
    const pageY = e.pageY;
    x.innerHTML = pageX;
    y.innerHTML = pageY;
    note.style.top = eval(pageY + " + 10") + "px";
    note.style.left = eval(pageX + " + 10") + "px";
    teal_top.style.top = eval(pageY + " - 1") + "px";
    teal_left.style.left = eval(pageX + " - 1") + "px";
}
initImage();
initLayout();