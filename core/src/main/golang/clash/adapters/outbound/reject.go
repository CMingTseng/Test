package outbound

import (
	"context"
	"errors"
	"io"
	"net"
	"time"

	"github.com/Dreamacro/clash/common/cache"
	C "github.com/Dreamacro/clash/constant"
)

const rejectCountLimit = 50
const rejectDelay = time.Second * 30

var rejectCounter = cache.NewLRUCache(cache.WithAge(10), cache.WithStale(false), cache.WithSize(128))

type Reject struct {
	*Base
}

// DialContext implements C.ProxyAdapter
func (r *Reject) DialContext(ctx context.Context, metadata *C.Metadata) (C.Conn, error) {
	key := metadata.RemoteAddress()

	count, existed := rejectCounter.Get(key)
	if !existed {
		count = 0
	}

	count = count.(int) + 1

	rejectCounter.Set(key, count)

	if count.(int) > rejectCountLimit {
		c, _ := net.Pipe()

		c.SetDeadline(time.Now().Add(rejectDelay))

		return NewConn(c, r), nil
	}

	return NewConn(&NopConn{}, r), nil
}

// DialUDP implements C.ProxyAdapter
func (r *Reject) DialUDP(metadata *C.Metadata) (C.PacketConn, error) {
	return nil, errors.New("match reject rule")
}

func NewReject() *Reject {
	return &Reject{
		Base: &Base{
			name: "REJECT",
			tp:   C.Reject,
			udp:  true,
		},
	}
}

type NopConn struct{}

func (rw *NopConn) Read(b []byte) (int, error) {
	return 0, io.EOF
}

func (rw *NopConn) Write(b []byte) (int, error) {
	return 0, io.EOF
}

// Close is fake function for net.Conn
func (rw *NopConn) Close() error { return nil }

// LocalAddr is fake function for net.Conn
func (rw *NopConn) LocalAddr() net.Addr { return nil }

// RemoteAddr is fake function for net.Conn
func (rw *NopConn) RemoteAddr() net.Addr { return nil }

// SetDeadline is fake function for net.Conn
func (rw *NopConn) SetDeadline(time.Time) error { return nil }

// SetReadDeadline is fake function for net.Conn
func (rw *NopConn) SetReadDeadline(time.Time) error { return nil }

// SetWriteDeadline is fake function for net.Conn
func (rw *NopConn) SetWriteDeadline(time.Time) error { return nil }
