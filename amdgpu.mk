O ?= build

.PHONY: clean all libdrm mesa

all: libdrm mesa

# Depending on a directory we are outputting into should
# always trigger a re-build. This is desired as we are
# using make mostly for dependencies
$(O):
	mkdir -p $(O)

libdrm: $(O)
	mkdir -p $(O)/$@/
	cd $@ && ./autogen.sh --prefix="$(abspath $(O)/$@/)"
	cd $@ && $(MAKE)
	cd $@ && $(MAKE) install

mesa: $(O) libdrm
	mkdir -p $(O)/$@/
	cd $@ && ./autogen.sh \
		--prefix="$(abspath $(O)/$@/)" \
		--enable-dri3 \
		--enable-debug=yes \
		--enable-glx-tls \
		--enable-texture-float \
		--enable-llvm \
		--with-vulkan-drivers=radeon,intel \
		--with-icd-dir=/etc/vulkan/icd.d \
		--with-dri-drivers=radeon,i965 \
		--with-gallium-drivers=r300,r600,radeonsi \
		--with-llvm-prefix=/opt/llvm \
		PKG_CONFIG_PATH=$(O)/$@/libdrm/lib/pkgconfig
	cd $@ && $(MAKE)
	cd $@ && $(MAKE) install

# The checkout command should cleaned all the git repos
# When possible, intermediate build output should also
# go to build
clean:
	rm -rf $(O)
