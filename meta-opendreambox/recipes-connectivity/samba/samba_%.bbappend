FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://smb.conf"

EXTRA_OECONF += "--without-ads --without-cluster-support --with-static-modules=!DEFAULT,!FORCED"

SAMBA4_LIBS = "NONE"
SAMBA4_MODULES = "!DEFAULT,!FORCED,auth_unix,pdb_tdbsam"

do_install_append() {
    install -d ${D}${sysconfdir}/samba
    install -m 0644 ${WORKDIR}/smb.conf ${D}${sysconfdir}/samba
}
