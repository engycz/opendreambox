SUMMARY = "Skins for Enigma2"
LICENSE = "CLOSED"
SRCREV = "${@opendreambox_srcrev('b3a46e566b5e6f6904d087d4a930494b094758cd', d)}"

inherit allarch autotools-brokensep opendreambox-github

PACKAGES_DYNAMIC = "^enigma2-skin-.*"
PACKAGES += "${PN}-meta"

FILES_${PN} = ""
FILES_${PN}-meta = "${datadir}/meta"

python enigma2_skins_split_packages() {
    output_pattern = 'enigma2-skin-%s'
    enigma2_skindir = os.path.join(d.getVar('datadir', True), 'enigma2')
    do_split_packages(d, enigma2_skindir, '(.*?)/.*', output_pattern, 'Enigma2 Skin: %s', recursive=True, extra_depends='enigma2', match_path=True)

    def parseControlFile(package, dir):
        filename = os.path.join(dir, 'CONTROL/control')
        if os.path.exists(filename):
            with open(filename) as f:
                for line in f.read().splitlines():
                    try:
                        name, value = line.strip().split(': ', 1)
                        if name == 'Description':
                            d.setVar('DESCRIPTION_%s' % package, value)
                        elif name in ('Breaks', 'Conflicts', 'Depends', 'Provides', 'Recommends', 'Replaces', 'Suggests'):
                            d.appendVar('R%s_%s' % (name.upper(), package), ' ' + ' '.join(value.split(', ')))
                    except:
                        bb.fatal("Error parsing control file for package %s" % package)

    srcdir = os.path.join(d.getVar('S', True), 'skins')
    packages = d.getVar('PACKAGES', True).split()
    for subdir in next(os.walk(srcdir))[1]:
        package = output_pattern % legitimize_package_name(subdir)
        if package in packages:
            parseControlFile(package, os.path.join(srcdir, subdir))
}

PACKAGESPLITFUNCS_prepend = "enigma2_skins_split_packages "

# No package will be generated for ${PN}, and enigma2-plugins doesn't exist as
# a package either, but it's enough to avoid a direct build dependency.
RDEPENDS_${PN} = "enigma2 enigma2-plugins"
