# jim: small footprint TCL interpreter

SRC_URI = "git://github.com/msteveb/jimtcl.git;protocol=https;branch=${SRCBRANCH}"
SRCBRANCH = "master"
#SRCREV = "a77ef1a6218fad4c928ddbdc03c1aedc41007e70"
#SRCREV = "fb923fab4f0cf276c336d98692d00df6a943791d"
SRCREV = "0545f4c08c65836104d0373879b5173d885ee9a4"

S = "${WORKDIR}/git"
B = "${S}"

LICENSE = "BSD-2-Clause-Views"
LIC_FILES_CHKSUM = " \
	file://${S}/LICENSE;md5=d69300147248518155ea330e78019033 \
	"

BBCLASSEXTEND = "native nativesdk"

inherit pkgconfig

EXTRA_CONF = "--host=${HOST_SYS} --build=${TARGET_SYS} --shared --disable-docs --math"
EXTRA_CONF:append:class-target = " --prefix=${prefix} --sysroot='${RECIPE_SYSROOT}'"
EXTRA_CONF:append:class-native = " --prefix=${base_prefix}/usr --sysroot='${RECIPE_SYSROOT_NATIVE}'"

EXTRA_OEMAKE = " \
	INSTALL_DATA_DIR='install -d -m 755' \
	INSTALL_DATA='install -m 644' \
	INSTALL_PROGRAM='install -m 755' \
	"

do_configure() {
	./make-bootstrap-jim >autosetup/jimsh0.c
	./configure ${EXTRA_CONF}
}

do_compile() {
	oe_runmake
}
do_install() {
	oe_runmake 'DESTDIR=${D}' install
}

# package the Jim shell and debugger separately
PACKAGES =+ "${PN}sh ${PN}db"

FILES:${PN}sh = "${prefix}/bin/jimsh"
RDEPENDS:${PN}sh = "${PN}"

FILES:${PN}db = "${prefix}/bin/jimdb"
RDEPENDS:${PN}db = "${PN}sh"
