O ?= build
BUILD_SUBDIR ?= build
ROOT = $(PWD)

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

llvm: $(O)
	mkdir -p $(O)/$(BUILD_SUBDIR)/$@/
	mkdir -p $(O)/$@/
	cd $(O)/$(BUILD_SUBDIR)/$@/ && cmake $(ROOT)/$@ \
		-DCMAKE_BUILD_TYPE=Release \
		-DLLVM_LINK_LLVM_DYLIB=true \
		-DLLVM_CCACHE_BUILD=true \
		-DCMAKE_INSTALL_PREFIX="$(abspath $(O)/$@/)"
	cd $(O)/$(BUILD_SUBDIR)/$@/ && $(MAKE)
	cd $(O)/$(BUILD_SUBDIR)/$@/ && $(MAKE) install

mesa: $(O) libdrm llvm
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
		--with-llvm-prefix="$(abspath $(O)/llvm)" \
		PKG_CONFIG_PATH="$(abspath $(O)/libdrm/lib/pkgconfig/)"
	cd $@ && $(MAKE)
	cd $@ && $(MAKE) install

# The checkout command should cleaned all the git repos
# When possible, intermediate build output should also
# go to build
clean:
	rm -rf $(O)
